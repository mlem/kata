package magic

import spock.lang.Specification


class GameSpec extends Specification {
    Magic magic = new Magic()

    Match match

    def setup() {
        magic.matchFactory = [
                newMatch: { firstPlayer, secondPlayer ->
                    def match = new TwoPlayerMatch(
                            determinePlayerForTurnOne: new DeterminePlayerForTurnOne() {
                                Player from(Player... players) {
                                    return players[0]
                                }
                            },
                            firstPlayer: firstPlayer,
                            secondPlayer: secondPlayer
                    )
                    firstPlayer.match = match
                    secondPlayer.match = match
                    return match
                }]

        match = magic.startNewMatch()
    }

    def "two players in a match start with 20 health each"() {
        expect:
        match.firstPlayer.health == 20
        match.secondPlayer.health == 20
    }

    def "the player with health 0 or less loses the match"() {
        match[player].health = 0
        expect:
        match.findLoser() == match[player]
        where:
        player << ["firstPlayer", "secondPlayer"]
    }

    def "the player with zero cards in his deck loses the match on his next draw"() {
        match = magic.startNewMatch()
        match[player].deck = new Deck()
        match[enemy].deck = new Deck(cards: [new Card()])
        match.nextTurn()
        when:
        match.currentPlayer.draws()
        then:
        match.findLoser() == match[player]
        where:
        player << ["firstPlayer", "secondPlayer"]
        enemy << ["secondPlayer", "firstPlayer"]
    }


    def "match starts with both players hp at 20"() {
        Match match = magic.startNewMatch()
        expect:
        match.firstPlayer.health == 20
        match.secondPlayer.health == 20
    }

    def "match has a decisionMaker about who starts the match"() {
        match = magic.startNewMatch()
        when:
        match.nextTurn()
        then:
        match.currentPlayer == match.firstPlayer
    }

    def "match over?"() {
        match[player].health = 0
        expect:
        match.isOver() == true
        where:
        player << ["firstPlayer", "secondPlayer"]
    }

    def "a player draws a card from his deck to his hand"() {
        def player = new Player()
        player.deck = new Deck(cards: [new Card(name: "A")])
        when:
        player.drawCard()
        then:
        player.hand.size() == 1
        player.hand[0].name == "A"
        player.deck.size() == 0
    }

    def "when a match is created the decks are shuffled"() {
        given:
        def match = GroovyMock(Match)
        magic.matchFactory = [newMatch: { a, b -> match }]

        when:
        magic.startNewMatch()

        then:
        1 * match.shufflePlayerDecks()
    }

    def "when a match is shuffling player decks, each player shuffles his own deck"() {
        given:
        match.firstPlayer = GroovyMock(Player)
        match.secondPlayer = GroovyMock(Player)

        when:
        match.shufflePlayerDecks()

        then:
        1 * match.firstPlayer.shuffleDeck()
        1 * match.secondPlayer.shuffleDeck()
    }

    def "a shuffler which reverses the order of the deck"() {
        given:
        Shuffler shuffler = new ReversingShuffler()

        when:
        Deck deck = shuffler.shuffle(new Deck(cards: [new Card(name: "A"), new Card(name: "B")]))

        then:
        deck*.name == ["B", "A"]
    }

    def "on start both players draw a hand"() {
        given:
        magic.firstPlayer = GroovyMock(Player)
        magic.secondPlayer = GroovyMock(Player)

        when:
        match = magic.startNewMatch()

        then:
        1 * magic.firstPlayer.drawHand()
        1 * magic.secondPlayer.drawHand()
    }

    def "player draws hand"() {
        given:
        match.handSize = handSize
        def cards = (1..handSize).collect {new Card(name:"c$it")}
        Player player = new Player(deck: new Deck(cards: cards), match: match)

        when:
        player.drawHand()

        then:
        player.hand == cards.reverse()
        player.deck.isEmpty()

        where:
        handSize << [2, 3]
    }

    class NoOpShuffler implements Shuffler {

        @Override
        Deck shuffle(Deck deck) {
            return deck
        }
    }

    interface Shuffler {
        Deck shuffle(Deck deck)
    }

    class ReversingShuffler implements Shuffler {

        @Override
        Deck shuffle(Deck deck) {
            return new Deck(cards: deck.reverse())
        }
    }

    class Card {

        String name
    }

    class Deck {
        @Delegate
        LinkedList cards = []

        Card nextCard() {
            return (Card)cards.pop()
        }
    }

    interface DeterminePlayerForTurnOne {
        Player from(Player... players)
    }

    class Magic {

        def matchFactory
        Player firstPlayer = new Player(health: 20, name: "first", deck: new Deck(cards: []))
        Player secondPlayer = new Player(health: 20, name: "second", deck: new Deck(cards: []))

        Match startNewMatch() {
            Match match = matchFactory.newMatch(firstPlayer, secondPlayer)
            match.shufflePlayerDecks()
            match.drawPlayerHands()
            return match
        }
    }

    interface Match {
        Player findLoser()

        boolean isOver()

        Player getCurrentPlayer()

        Player getFirstPlayer()

        Player getSecondPlayer()

        void nextTurn()

        void drawPlayerHands()

        def getHandSize()
    }

    class TwoPlayerMatch implements Match {

        Player firstPlayer
        Player secondPlayer
        Player currentPlayer
        DeterminePlayerForTurnOne determinePlayerForTurnOne
        def handSize = 2

        Player findLoser() {
            if (firstPlayer.health <= 0) {
                return firstPlayer
            }
            if (secondPlayer.health <= 0) {
                return secondPlayer
            }
            if (firstPlayer.deck.cards.isEmpty()) {
                return firstPlayer
            }
            if (secondPlayer.deck.cards.isEmpty()) {
                return secondPlayer
            }
        }

        boolean isOver() {
            findLoser()
        }

        void nextTurn() {
            currentPlayer = determinePlayerForTurnOne.from(firstPlayer, secondPlayer)
        }

        @Override
        void drawPlayerHands() {
            firstPlayer.drawHand()
            secondPlayer.drawHand()
        }


        def shufflePlayerDecks() {
            firstPlayer.shuffleDeck()
            secondPlayer.shuffleDeck()
        }
    }

    class Player {
        def health
        def name
        Deck deck
        List hand = []
        Match match

        String toString() {
            "Player $name"
        }

        def drawCard() {
            if (!deck.isEmpty())
                hand << deck.nextCard()
        }

        void shuffleDeck() {
        }

        def draws() {

        }

        def drawHand() {
            match.handSize.times { drawCard() }
        }
    }

}
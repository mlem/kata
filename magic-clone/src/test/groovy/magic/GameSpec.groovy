package magic

import spock.lang.Specification


class GameSpec extends Specification {
    Magic magic = new Magic()

    Match match

    def setup() {
        magic.matchFactory = [
                newMatch: {firstPlayer, secondPlayer ->
                    new TwoPlayerMatch(
                            determinePlayerForTurnOne: new DeterminePlayerForTurnOne() {
                                Player from(Player... players) {
                                    return players[0]
                                }
                            },
                            firstPlayer: firstPlayer,
                            secondPlayer: secondPlayer
                    )
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
        player.drawACard()
        then:
        player.hand.size() == 1
        player.hand[0].name == "A"
        player.deck.cards.size() == 0
    }

    def "when a match is created the decks are shuffled"() {
        given:
        def match = GroovyMock(Match)
        magic.matchFactory = [newMatch: {a, b -> match }]

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
        deck.cards*.name == ["B", "A"]
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
            return new Deck(cards: deck.cards.reverse())
        }
    }

    class Card {

        String name
    }

    class Deck {

        List cards = []

        Card nextCard() {
            return cards.pop()
        }
    }

    interface DeterminePlayerForTurnOne {
        Player from(Player... players)
    }

    class Magic {

        def matchFactory

        Match startNewMatch() {
            def match = matchFactory.newMatch(new Player(health: 20, name: "first", deck: new Deck(cards: [])), new Player(health: 20, name: "second", deck: new Deck(cards: [])))
            match.shufflePlayerDecks()
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
    }

    class TwoPlayerMatch implements Match {

        Player firstPlayer
        Player secondPlayer
        Player currentPlayer
        DeterminePlayerForTurnOne determinePlayerForTurnOne

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

        String toString() {
            "Player $name"
        }

        def drawACard() {
            hand << deck.nextCard()
        }

        void shuffleDeck() {

        }

        def draws() {

        }
    }

}
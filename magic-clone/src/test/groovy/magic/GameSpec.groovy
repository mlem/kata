package magic

import spock.lang.Ignore
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

    @Ignore ("try to use some observer pattern (HealthObserver)")
    def "the player with health 0 or less loses the match"() {
        given:
        match[player].health = 0

        expect:
        match.getLoser() == match[player]

        where:
        player << ["firstPlayer", "secondPlayer"]
    }

    def "first player loses because of deck starvation"() {
        given:
        match.firstPlayer.deck = new Deck()

        when:
        match.nextTurn()

        then:
        match.loser == match.firstPlayer

    }

    def "second player loses because of deck starvation"() {
        given:
        match.firstPlayer.deck = new Deck(cards: [new Card()])
        match.secondPlayer.deck = new Deck()

        when:
        match.nextTurn()
        match.nextTurn()

        then:
        match.loser == match.secondPlayer

    }


    def "match starts with both players hp at 20"() {
        expect:
        match.firstPlayer.health == 20
        match.secondPlayer.health == 20
    }

    def "match has a decisionMaker about who starts the match"() {
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
        def player = new Player(match:new TwoPlayerMatch())
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
        def cards = (1..handSize).collect { new Card(name: "c$it") }
        Player player = new Player(deck: new Deck(cards: cards), match: match)

        when:
        player.drawHand()

        then:
        player.hand == cards.reverse()
        player.deck.isEmpty()

        where:
        handSize << [2, 3]
    }

    def "when match has next turn, player is notified"() {
        given:
        match.firstPlayer = GroovyMock(Player)
        match.secondPlayer = GroovyMock(Player)
        when:
        match.nextTurn()
        then:
        1 * match.firstPlayer.whatDoYouWantToDoThisTurn()
        when:
        match.nextTurn()
        then:
        1 * match.secondPlayer.whatDoYouWantToDoThisTurn()
        when:
        match.nextTurn()
        then:
        1 * match.firstPlayer.whatDoYouWantToDoThisTurn()
    }

    def "a player draws a card on play turn"() {
        given:
        match.firstPlayer = GroovyMock(Player)
        match.secondPlayer = GroovyMock(Player)
        when:
        match.nextTurn()
        then:
        1 * match.firstPlayer.drawCard()
        when:
        match.nextTurn()
        then:
        1 * match.secondPlayer.drawCard()
        when:
        match.nextTurn()
        then:
        1 * match.firstPlayer.drawCard()
    }

    def "a game with cpu controlled players"() {
        given:
        magic.firstPlayer = new DumbBotPlayer(deck:  new Deck(cards: [new Card(name:"A"), new Card(name:"B"), new Card(name:"C")]) )
        magic.secondPlayer = new DumbBotPlayer(deck:  new Deck(cards: [new Card(name:"X"), new Card(name:"Y"), new Card(name:"Z")]) )

        when:
        def match = magic.startNewMatch()
   //     match.nextTurn()

        then:
        match.timeline*.name == ["dA", "dB", "dX", "dY"]
       // match.timeline*.name == ["dA", "dB", "dX", "dY", "dC", "pA", "dZ", "pX"]
        //match.timeline*.name == ["AXBYCZ"]
    }

    class DumbBotPlayer extends Player {
        @Override
        void shuffleDeck() {
            deck = new ReversingShuffler().shuffle(deck)
        }
    }

    def "on deck starvation player is declaring lose"() {
        given:
        Player player = new Player()
        player.match = GroovyMock(Match)
        player.deck = new Deck()

        when:
        player.drawCard()

        then:
        1 * player.match.declareLoser(player)
    }

    def "declare loser gives us a loser and a winner"() {

        when:
        match.declareLoser(match.firstPlayer)

        then:
        match.loser == match.firstPlayer
        match.winner == match.secondPlayer
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
            return (Card) cards.pop()
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
        Player getLoser()

        boolean isOver()

        Player getCurrentPlayer()

        Player getFirstPlayer()

        Player getSecondPlayer()

        void nextTurn()

        void drawPlayerHands()

        def getHandSize()

        def declareLoser(Player player)

        Player getWinner()

        def getTimeline()
    }

    class TwoPlayerMatch implements Match {

        Player firstPlayer
        Player secondPlayer
        Player currentPlayer
        Player winner
        List timeline = []
        DeterminePlayerForTurnOne determinePlayerForTurnOne
        def handSize = 2

        Player getLoser() {
            winner == firstPlayer ? secondPlayer : firstPlayer
        }

        boolean isOver() {
            getLoser()
        }

        void nextTurn() {
            if (currentPlayer == null)
                currentPlayer = determinePlayerForTurnOne.from(firstPlayer, secondPlayer)
            else
                currentPlayer = currentPlayer == firstPlayer ? secondPlayer : firstPlayer
            currentPlayer.drawCard()
            currentPlayer.whatDoYouWantToDoThisTurn()
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

        @Override
        def declareLoser(Player player) {
            winner = player == firstPlayer ? secondPlayer : firstPlayer
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
            if (!deck.isEmpty())  {
                hand << deck.nextCard()
                //match.drawn(hand.last())
                // match.drawn(this)
                match.timeline << [name: "d" + hand.last().name]
            }
            else
                match.declareLoser(this)
        }

        void shuffleDeck() {
        }

        def draws() {

        }

        def drawHand() {
            match.handSize.times { drawCard() }
        }

        def whatDoYouWantToDoThisTurn() {
        }
    }

}
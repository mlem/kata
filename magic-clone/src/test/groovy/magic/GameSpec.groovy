package magic

import spock.lang.Specification


class GameSpec extends Specification {
    Magic magic = new Magic()

    Match game

    def setup() {
        magic.gameFactory = [
                newGame: {firstPlayer, secondPlayer ->
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

        game = magic.startNewGame()
    }

    def "two players in a game start with 20 health each"() {
        expect:
        game.firstPlayer.health == 20
        game.secondPlayer.health == 20
    }

    def "the player with health 0 or less loses the game"() {
        game[player].health = 0
        expect:
        game.findLoser() == game[player]
        where:
        player << ["firstPlayer", "secondPlayer"]
    }

    def "the player with zero cards in his deck loses the game on his next draw"() {
        game = magic.startNewGame()
        game[player].deck = new Deck()
        game[enemy].deck = new Deck(cards: [new Card()])
        game.nextTurn()
        when:
        game.currentPlayer.draws()
        then:
        game.findLoser() == game[player]
        where:
        player << ["firstPlayer", "secondPlayer"]
        enemy << ["secondPlayer", "firstPlayer"]
    }


    def "game starts with both players hp at 20"() {
        Match game = magic.startNewGame()
        expect:
        game.firstPlayer.health == 20
        game.secondPlayer.health == 20
    }

    def "game has a decisionMaker about who starts the game"() {
        game = magic.startNewGame()
        when:
        game.nextTurn()
        then:
        game.currentPlayer == game.firstPlayer
    }

    def "game over?"() {
        game[player].health = 0
        expect:
        game.isOver() == true
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

    def "when a game is created the decks are shuffled"() {
        given:
        def game = GroovyMock(Match)
        magic.gameFactory = [newGame: {a, b -> game }]

        when:
        magic.startNewGame()

        then:
        1 * game.shufflePlayerDecks()
    }

    def "when a game is shuffling player decks, each player shuffles his own deck"() {
        given:
        game.firstPlayer = GroovyMock(Player)
        game.secondPlayer = GroovyMock(Player)

        when:
        game.shufflePlayerDecks()

        then:
        1 * game.firstPlayer.shuffleDeck()
        1 * game.secondPlayer.shuffleDeck()
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

        def gameFactory

        Match startNewGame() {
            def game = gameFactory.newGame(new Player(health: 20, name: "first", deck: new Deck(cards: [])), new Player(health: 20, name: "second", deck: new Deck(cards: [])))
            game.shufflePlayerDecks()
            return game
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
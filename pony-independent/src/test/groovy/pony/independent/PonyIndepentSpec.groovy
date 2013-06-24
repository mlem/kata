package pony.independent

import spock.lang.Specification
import spock.lang.Unroll

@Deprecated
@Unroll
class PonyIndepentSpec extends Specification {

    def "test"() {
        expect:
        canInvite(input) == expected

        where:
        input           | expected
        "0 0"           | 0
        "1 0"           | 1
        "5 0"           | 5
        "2 1\n0 1"      | 1
        "3 1\n0 1"      | 2
        "3 1\n1 2"      | 2
        "3 2\n0 1\n0 2" | 2
        "4 1\n0 1"      | 3
    }

    /**
     * + 0 1 2  + 0 1 2
     * 0   x x  0 0 1 1 2
     * 1 x      1 1 0 0 1
     * 2 x      2 1 0 0 1
     *                + -
     *                  2
     *
     * [[0, 0],
     *
     *  [0, 0]]
     */
    // every pony w/o a neighbour gets invited
    //
    // a pony with just one neighbour can be invitied, if you dismiss his neighbour
    def canInvite(String input) {
        def lines = input.readLines().collect { it.split(' ')*.toInteger() }
        def numberOfPonies = lines[0][0]
        def records = lines[0][1]
        if (numberOfPonies == 0)
            return 0
        if (numberOfPonies == 1)
            return 1
        if (records == 0) {
            return numberOfPonies
        }

        def graph = (0..numberOfPonies - 1).collect([]) { new Node(number: it) }
        lines.subList(1, lines.size()).each { data ->
            graph.find { it.number == data[0] }.link(graph.find { it.number == data[1] })
        }
        graph.sort { x, y -> x.connectionCount <=> y.connectionCount }

        while (graph.last().connectionCount > 0) {
            def node = graph.last()
            node.remove()
            graph.remove(node)
        }

        return graph.size()
    }


    class Node {
        def number
        def links = []

        def link(node) {
            if (links.contains(node)) {
                return
            }
            links << node
            node.link(this)
        }

        def getConnectionCount() { links.size() }

        def remove() {
            links*.remove(this)
        }

        def remove(node) {
            links.remove(node)
        }
    }
}
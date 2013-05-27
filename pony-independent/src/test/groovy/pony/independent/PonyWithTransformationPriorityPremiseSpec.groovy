package pony.independent

import org.hamcrest.CoreMatchers
import org.junit.Assert
import spock.lang.Specification
import spock.lang.Unroll

import static org.hamcrest.CoreMatchers.either
import static org.hamcrest.CoreMatchers.hasItem

/**
 * Created with IntelliJ IDEA.
 * User: BeoXTC
 * Date: 20.05.13
 * Time: 21:57
 * To change this template use File | Settings | File Templates.
 */
@Unroll
class PonyWithTransformationPriorityPremiseSpec extends Specification {

    /**
     1.({}–>nil) no code at all->code that employs nil
     2.(nil->constant)
     3.(constant->constant+) a simple constant to a more complex constant
     4.(constant->scalar) replacing a constant with a variable or an argument
     5.(statement->statements) adding more unconditional statements.
     6.(unconditional->if) splitting the execution path
     7.(scalar->array)
     8.(array->container)
     9.(statement->recursion)
     10.(if->while)
     11.(expression->function) replacing an expression with a function or algorithm
     12.(variable->assignment) replacing the value of a variable.
     */


    def "using constants" () {
          expect:
        poniesToInvite('0 0') == []
        poniesToInvite('1 0')*.toMap() == [[0:[]]]
        poniesToInvite('2 0')*.toMap() == [[0:[]], [1:[]]]
        poniesToInvite('2 1\n0 1')*.toMap() == [[0:[1]], [1:[0]]]

    }

    def "parsing input"() {
        expect:
        parseInput('0 0').numberOfPonies == 0
        parseInput('0 0').records == []
        parseInput('1 0').numberOfPonies == 1
        parseInput('1 0').records == []
        parseInput('2 1\n0 1').numberOfPonies == 2
        parseInput('2 1\n0 1').records == [[0, 1]]

    }

    def "testing sorting by connection count"() {
        expect:
        poniesToInvite('3 2\n0 1\n1 2')*.toMap() == [[0:[1]], [1:[0, 2]], [2:[1]]]
        partySort(poniesToInvite('3 2\n0 1\n1 2'))*.toMap() == [[1:[0, 2]], [0:[1]], [2:[1]]]
    }

    def "drop one with most relationships"() {
        expect:
        dropLargest(poniesToInvite('3 2\n0 1\n1 2'))*.toMap() == [[0:[]], [2:[]]]
        dropLargest(poniesToInvite('5 3\n1 2\n2 3\n3 4'))*.toMap() as Set == [[0:[]], [1:[]], [3:[4]], [4:[3]]] as Set
    }

    def "drop relations until there are no more"() {
        given:
        def graph = poniesToInvite('5 3\n1 2\n2 3\n3 4')

        when:
        while(graph.any {!it.relations.empty}) {
            graph = dropLargest(graph)
        }

        then:
        graph.size() == 3
        graph*.node.findAll {it in [3, 4]} != [3, 4]
        graph.each { assert it.node in [0,1] || it.node == 3 || it.node == 4}
        //graph*.node as Set == [0, 1, { it == 3 || it == 4}]
        Assert.assertThat(graph*.node, CoreMatchers.is(either(hasItem(3)).or(hasItem(4))))
    }

    def dropLargest(List list) {
        def sortedList = partySort(list)
        def removedPony = list.remove(0)
        list.each {
            it.relations -= removedPony
        }
        list
    }

    def partySort(List input) {
        input.sort {VertexNode a, VertexNode b ->
            b.relations.size().compareTo(a.relations.size())
        }
    }

    def parseInput(String input) {
        def data = input.readLines()*.split(' ').collect {it*.toInteger()}
        def records = data.subList(1, data.size())
        new PonyDTO(numberOfPonies: data[0][0], records: records)
    }


    def poniesToInvite(data) {
        def ponyData = parseInput(data)
        def numberOfPonies = ponyData.numberOfPonies
        if(numberOfPonies == 0) return []
        def graph = initializeGraph(numberOfPonies)
        ponyData.records.each { edge ->
            graph[edge[0]].relations << graph[edge[1]]
            graph[edge[1]].relations << graph[edge[0]]
        }
        graph
    }

    private def initializeGraph(int numberOfPonies) {
        (0..numberOfPonies - 1).collect([]) {

            return new VertexNode(node:it)
        }
    }

    class VertexNode {
        def node
        def relations = []

        Map toMap() {
            def map = [:]
            map.put(node, relations*.node)
            return map
        }
    }

    class PonyDTO {
        int numberOfPonies
        def records
    }
}

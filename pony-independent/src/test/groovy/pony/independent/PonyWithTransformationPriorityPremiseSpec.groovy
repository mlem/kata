package pony.independent

import spock.lang.Specification
import spock.lang.Unroll

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
        poniesToInvite('1 0') == [[0:[]]]
        poniesToInvite('2 0') == [[0:[]], [1:[]]]
        poniesToInvite('2 1\n0 1') == [[0:[1]], [1:[0]]]

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


    def partySort(List input) {
        input.sort {Map a, Map b ->
            b.values().toArray()[0].size().compareTo(a.values().toArray()[0].size())
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
        def graph = initializaGraph(numberOfPonies)
        ponyData.records.each { edge ->
            graph[edge[0]][edge[0]] << edge[1]
            graph[edge[1]][edge[1]] << edge[0]
        }
        graph
    }

    private def initializaGraph(int numberOfPonies) {
        (0..numberOfPonies - 1).collect([]) {
            def map = [:]
            map.put(it, [])
            return map
        }
    }

    class PonyDTO {
        int numberOfPonies
        def records = []
    }
}

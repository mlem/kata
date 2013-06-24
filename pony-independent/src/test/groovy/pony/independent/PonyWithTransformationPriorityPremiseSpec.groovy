package pony.independent

import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Ignore
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
          new PonyInteraction().parseGraph('0 0') == []
        new PonyInteraction().parseGraph('1 0')*.toMap() == [[0:[]]]
        new PonyInteraction().parseGraph('2 0')*.toMap() == [[0:[]], [1:[]]]
        new PonyInteraction().parseGraph('2 1\n0 1')*.toMap() == [[0:[1]], [1:[0]]]

    }

    def "parsing input"() {
        expect:
        new PonyInteraction().toRawData('0 0').numberOfPonies == 0
        new PonyInteraction().toRawData('0 0').records == []
        new PonyInteraction().toRawData('1 0').numberOfPonies == 1
        new PonyInteraction().toRawData('1 0').records == []
        new PonyInteraction().toRawData('2 1\n0 1').numberOfPonies == 2
        new PonyInteraction().toRawData('2 1\n0 1').records == [[0, 1]]

    }

    def "testing sorting by connection count"() {
        expect:
        new PonyInteraction().parseGraph('3 2\n0 1\n1 2')*.toMap() == [[0:[1]], [1:[0, 2]], [2:[1]]]
        new PonyInteraction().partySort(new PonyInteraction().parseGraph('3 2\n0 1\n1 2'))*.toMap() == [[1:[0, 2]], [0:[1]], [2:[1]]]
    }

    def "drop one with most relationships"() {
        expect:
        new PonyInteraction().dropLargest(new PonyInteraction().parseGraph('3 2\n0 1\n1 2'))*.toMap() == [[0:[]], [2:[]]]
        new PonyInteraction().dropLargest(new PonyInteraction().parseGraph('5 3\n1 2\n2 3\n3 4'))*.toMap() as Set == [[0:[]], [1:[]], [3:[4]], [4:[3]]] as Set
    }

    def "drop relations until there are no more"() {
        expect:
        new PonyInteraction(input: '5 3\n1 2\n2 3\n3 4').getNumberOfPoniesToInvite() == 3

    }

    //FIXME Bug in our code to fix
    @Ignore
    def "special case - prefer nodes with endpoint neighbours"() {
        def input = """10 9
0 1
0 3
2 4
8 3
0 7
3 9
6 2
8 6
9 5"""
        expect:
        new PonyInteraction(input: input).getNumberOfPoniesToInvite() == 6




    }


    @Ignore
    def "integration test"() {
        given:
        def input = getClass().getResourceAsStream("/P/${number}.in".toString()).text
        def output = getClass().getResourceAsStream("/2012_preec_outputs/P/P${number}.out".toString()).text

        expect:
        new PonyInteraction(input: input).getNumberOfPoniesToInvite() == output.toInteger()

        where:
        number << [2] //(1..10)
    }

}

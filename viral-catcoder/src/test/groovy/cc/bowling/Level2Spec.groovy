package cc.bowling

import spock.lang.Ignore
import spock.lang.Specification

class Level2Spec extends Specification {

    def builder = new Builder()

    def "handle new input correctly"() {
        def input = "6 3 1 6 5 -2 4 1 1 -2 4"
        def pairOutput = "2 1 -2 3 -2"

        builder.input = input
        def permutation = builder.createPermutation()
        def result = permutation.calculatePairs()

        expect:
        assert result.toOutputString() == pairOutput
    }

    def "inversion from sample"() {
        def input = "6 3 1 6 5 -2 4 1 1 -2 4"
        def output = "3 1 2 -5 -6 4"

        builder.input = input
        def permutation = builder.createPermutation()
        def inversion = builder.createInversion()
        def result = inversion.invertPermutation(permutation)

        expect:
        assert result.toResultString() == output
    }
}

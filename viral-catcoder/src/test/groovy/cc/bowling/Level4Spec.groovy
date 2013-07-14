package cc.bowling

import spock.lang.Specification

class Level4Spec extends Specification {

    def "first sample"() {
        def input = "8 0 3 1 6 5 -2 4 7"
        def solution = new ViralChecker()
        def result = solution.calculateMinimumInversions(input)

        expect:
        assert result == 5
    }

    def "get biggest pair"() {
        def input = "8 0 3 1 6 5 -2 4 7"
        def builder = new Builder(input: input)
        def permutation = builder.createPermutation()
        when:
        def pairs = permutation.calculatePairs()
        then:
        pairs.toOutputString() == "2 1 -2 3 -2"
        when:
        def solution = new ViralChecker(builder: builder)
        def result = solution.findInversionWithMostPoints()

        then:
        result.secondNumber == -2
        result.firstNumber == 3
        result.firstIndex == 1
        result.secondIndex == 5
    }

}

package cc.bowling

import spock.lang.Ignore
import spock.lang.Specification

@Ignore
class Level2Spec extends Specification {

    def "handle new input correctly"() {
        def input = "6 3 1 6 5 -2 4 1 1 -2 4"
        def pairOutput = "2 1 -2 3 -2"

        def solution = new Solution()

        def result = solution.calculatePairs(input)

        expect:
        assert result.toPairOutput() == pairOutput
    }

    def "inversion from sample"() {
        def input = "6 3 1 6 5 -2 4 1 1 -2 4"
        def output = "3 1 2 -5 -6 4"

        def solution = new Solution()

        def result = solution.inversion(input)

        expect:
        assert result.toInversionOutput() == output
    }

    def "inversion from sample with integers"() {
        def input = [3, 1, 6, 5, -2, 4]
        def inversion = new Inversion(firstNumber: 1, firstIndex: 1, secondNumber: -2, secondIndex: 4)
        def output = [3, 1, 2, -5, -6, 4]

        def solution = new Solution()

        def result = solution.invert(input, inversion)

        expect:
        assert result == output
    }

    def "inversion on numbers first case xi + xj == 1"() {
        def input = [4, 2, 3, -1, 5]
        def inversion = new Inversion(firstIndex: 1, secondIndex: 3)

        def solution = new Solution()

        expect:
        solution.invert(input, inversion) == [4, -3, -2, -1, 5]
    }

    def "inversion on numbers first case xi + xj == -1"() {
        def input = [5, -2, 4, 1, 6]
        def inversion = new Inversion(firstIndex: 1, secondIndex: 3)

        def solution = new Solution()

        expect:
        solution.invert(input, inversion) == [5, -2, -1, -4, 6]
    }
}

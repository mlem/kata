package cc.bowling

import spock.lang.Ignore
import spock.lang.Specification;


class Level1Spec extends Specification {


    def "parsing String to numberlist"() {
        when:
        def result = new Parser().parse("6 3 1 6 5 -2 4")

        then:
        result == [6, 3, 1, 6, 5, -2, 4]
    }

    def "a oriented pair has a positive and negative number"() {
        when:
        def solution = new Solution().calculate([2, 0, -1])

        then:
        solution.pairs[0] == [0, -1]
        solution.anzahlPaare == 1
    }

    def "a oriented pair has to be +-1 "() {
        when:
        def solution = new Solution().calculate([2, 3, -1])

        then:
        solution.anzahlPaare == 0
    }


    def "with 2 permutations"() {
        when:
        def solution = new Solution().calculate([2, 2, -1])

        then:
        solution.pairs[0] == [2, -1]
        solution.anzahlPaare == 1
    }

    def "with 3 permutations"() {
        when:
        def solution = new Solution().calculate([3, 0, 2, -1])

        then:
        solution.pairs[0] == [0, -1]
        solution.pairs[1] == [2, -1]
        solution.anzahlPaare == 2
    }


    def "with 4 permutations"() {
        when:
        def solution = new Solution().calculate([4, 0, 3, 2, -1])

        then:
        solution.pairs[0] == [0, -1]
        solution.pairs[1] == [2, -1]
        solution.anzahlPaare == 2
    }

    def "test sample"() {
        def input = [6, 3, 1, 6, 5, -2, 4]
        def output = [2, 1, -2, 3, -2]

        def solution = new Solution()

        def result = solution.calculate(input)
        def anzahlPaare = output[0]
        def erstesPaar = [output[1], output[2]]
        def zweitesPaar = [output[3], output[4]]

        expect:
        assert result.anzahlPaare == anzahlPaare
        assert result.pairs[0] == erstesPaar
        assert result.pairs[1] == zweitesPaar
    }

    def "with string input"() {
        def input = "6 3 1 6 5 -2 4"
        def output = [2, 1, -2, 3, -2]

        def solution = new Solution()

        def result = solution.calculate(input)
        def anzahlPaare = output[0]
        def erstesPaar = [output[1], output[2]]
        def zweitesPaar = [output[3], output[4]]

        expect:
        assert result.anzahlPaare == anzahlPaare
        assert result.pairs[0] == erstesPaar
        assert result.pairs[1] == zweitesPaar
    }

    def "with string output"() {
        def input = "6 3 1 6 5 -2 4"
        def output = "2 1 -2 3 -2"

        def solution = new Solution()

        def result = solution.calculate(input)
        def anzahlPaare = output[0]
        def erstesPaar = [output[1], output[2]]
        def zweitesPaar = [output[3], output[4]]

        expect:
        assert result.toPairOutput() == output
    }

    def "it depends on the sorting of the array"() {
        when:
        def solution = new Solution().calculate([4, -2, 3, -5, 1])

        then:
        solution.pairs[0] == [-2, 1]
        solution.pairs[1] == [-2, 3]
        solution.anzahlPaare == 2
    }

    def "it has to work with longer integer arrays"() {
        when:
        def solution = new Solution().calculate([6, -2, 3, -5, 1, 4, 6])

        then:
        solution.pairs[0] == [-5, 4]
        solution.pairs[1] == [-5, 6]
        solution.pairs[2] == [-2, 1]
        solution.pairs[3] == [-2, 3]
        solution.anzahlPaare == 4
    }

    def "it has with whole left and right search"() {
        when:
        def solution = new Solution().calculate([6, 1, 6, -2, 3, -5, -4])

        then:
        solution.pairs[0] == [-2, 3]
        solution.pairs[1] == [1, -2]
        solution.pairs[2] == [3, -4]
        solution.pairs[3] == [6, -5]
        solution.anzahlPaare == 4
    }

    def "get a group of integers by their type"() {
        expect:
        new Solution().nextGroup([1, 2, -1, -2, -3, 1, 2, 3]) == [1, 2]
        new Solution().nextGroup([-1, -2, -3, 1, 2, 3]) == [-1, -2, -3]
        new Solution().nextGroup([1, 2, 3]) == [1, 2, 3]
    }


}

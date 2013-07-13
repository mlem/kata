package cc.bowling

import spock.lang.Ignore
import spock.lang.Specification;


class TestSpec extends Specification {

    def "integration test"() {
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
        assert result.toOutput() == output
    }


}

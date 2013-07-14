package cc.bowling

import spock.lang.Ignore
import spock.lang.Specification;


@Ignore
class Level1Spec extends Specification {

    def solution = new Solution()

    def "parsing String to numberlist"() {
        when:
        def result = new Parser().parse("6 3 1 6 5 -2 4")

        then:
        result == [6, 3, 1, 6, 5, -2, 4]
    }

    def "a oriented pair has a positive and negative number"() {
        when:
        def input = [2, 0, -1]
        this.solution.permutations = this.solution.filterPermutations(input)
        def solution = this.solution.calculatePairs(input)

        then:
        solution.pairs[0] == [0, -1]
        solution.anzahlPaare == 1
    }

    def "a oriented pair has to be +-1 "() {
        when:
        def input = [2, 3, -1]
        this.solution.permutations = this.solution.filterPermutations(input)
        def solution = this.solution.calculatePairs(input)

        then:
        solution.anzahlPaare == 0
    }


    def "with 2 permutations"() {
        when:
        def input = [2, 2, -1]
        this.solution.permutations = this.solution.filterPermutations(input)
        def solution = this.solution.calculatePairs(input)

        then:
        solution.pairs[0] == [2, -1]
        solution.anzahlPaare == 1
    }

    def "with 3 permutations"() {
        when:
        def input = [3, 0, 2, -1]
        this.solution.permutations = this.solution.filterPermutations(input)
        def solution = this.solution.calculatePairs(input)

        then:
        solution.pairs[0] == [0, -1]
        solution.pairs[1] == [2, -1]
        solution.anzahlPaare == 2
    }


    def "with 4 permutations"() {
        when:
        def input = [4, 0, 3, 2, -1]
        this.solution.permutations = this.solution.filterPermutations(input)
        def solution = this.solution.calculatePairs(input)

        then:
        solution.pairs[0] == [0, -1]
        solution.pairs[1] == [2, -1]
        solution.anzahlPaare == 2
    }

    def "test sample"() {
        def input = [6, 3, 1, 6, 5, -2, 4]
        def output = [2, 1, -2, 3, -2]

        this.solution.permutations = this.solution.filterPermutations(input)
        def solution = this.solution

        def result = solution.calculatePairs(input)
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

        def solution = this.solution

        def result = solution.calculatePairs(input)
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

        def solution = this.solution

        def result = solution.calculatePairs(input)

        expect:
        assert result.toPairOutput() == output
    }

    def "it depends on the sorting of the array"() {
        when:
        def input = [4, -2, 3, -5, 1]
        this.solution.permutations = this.solution.filterPermutations(input)
        def solution = this.solution.calculatePairs(input)

        then:
        solution.pairs[0] == [-2, 1]
        solution.pairs[1] == [-2, 3]
        solution.pairs[2] == [4, -5]
        solution.anzahlPaare == 3
    }

    def "it has to work with longer integer arrays"() {
        when:
        def input = [6, -2, 3, -5, 1, 4, 6]
        this.solution.permutations = this.solution.filterPermutations(input)
        def solution = this.solution.calculatePairs(input)

        then:
        solution.pairs[0] == [-5, 4]
        solution.pairs[1] == [-5, 6]
        solution.pairs[2] == [-2, 1]
        solution.pairs[3] == [-2, 3]
        solution.anzahlPaare == 4
    }

    def "it has with whole left and right search"() {
        when:
        def input = [6, 1, 6, -2, 3, -5, -4]
        this.solution.permutations = this.solution.filterPermutations(input)
        def solution = this.solution.calculatePairs(input)

        then:
        solution.pairs[0] == [-2, 3]
        solution.pairs[1] == [1, -2]
        solution.pairs[2] == [3, -4]
        solution.pairs[3] == [6, -5]
        solution.anzahlPaare == 4
    }

    def "get a group of integers by their type"() {
        expect:
        this.solution.nextGroup([1, 2, -1, -2, -3, 1, 2, 3]) == [1, 2]
        this.solution.nextGroup([-1, -2, -3, 1, 2, 3]) == [-1, -2, -3]
        this.solution.nextGroup([1, 2, 3]) == [1, 2, 3]
    }


}

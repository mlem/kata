package cc.bowling

import spock.lang.Specification;


class Level1Spec extends Specification {

    def builder = new Builder()

    def "parsing String to numberlist"() {
        when:
        def result = new Parser().parse("6 3 1 6 5 -2 4")

        then:
        result == [6, 3, 1, 6, 5, -2, 4]
    }

    def "a oriented pair has a positive and negative number"() {
        when:
        def input = [2, 0, -1]
        def permutation = builder.createPermutation(input)
        def solution = permutation.calculatePairs()

        then:
        solution[0].asList() == [0, -1]
        solution.size() == 1
    }

    def "a oriented pair has to be +-1 "() {
        when:
        def input = [2, 3, -1]
        def permutation = builder.createPermutation(input)
        def solution = permutation.calculatePairs()

        then:
        solution.size() == 0
    }


    def "with 2 permutations"() {
        when:
        def input = [2, 2, -1]
        def permutation = builder.createPermutation(input)
        def solution = permutation.calculatePairs()

        then:
        solution[0].asList() == [2, -1]
        solution.size() == 1
    }

    def "with 3 permutations"() {
        when:
        def input = [3, 0, 2, -1]
        def permutation = builder.createPermutation(input)
        def solution = permutation.calculatePairs()

        then:
        solution[0].asList() == [0, -1]
        solution[1].asList() == [2, -1]
        solution.size() == 2
    }


    def "with 4 permutations"() {
        when:
        def input = [4, 0, 3, 2, -1]
        def permutation = builder.createPermutation(input)
        def solution = permutation.calculatePairs()

        then:
        solution[0].asList() == [0, -1]
        solution[1].asList() == [2, -1]
        solution.size() == 2
    }

    def "test sample"() {
        def input = [6, 3, 1, 6, 5, -2, 4]
        def output = [2, 1, -2, 3, -2]

        def permutation = builder.createPermutation(input)
        def result = permutation.calculatePairs()
        def anzahlDerPaare = output[0]
        def erstesPaar = [output[1], output[2]]
        def zweitesPaar = [output[3], output[4]]

        expect:
        assert result.size() == anzahlDerPaare
        assert result[0].asList() == erstesPaar
        assert result[1].asList() == zweitesPaar
    }

    def "with string input"() {
        def input = "6 3 1 6 5 -2 4"
        def output = [2, 1, -2, 3, -2]

        builder.input = input
        def permutation = builder.createPermutation()
        def result = permutation.calculatePairs()
        def anzahlDerPaare = output[0]
        def erstesPaar = [output[1], output[2]]
        def zweitesPaar = [output[3], output[4]]

        expect:
        assert result.size() == anzahlDerPaare
        assert result[0].asList() == erstesPaar
        assert result[1].asList() == zweitesPaar
    }

    def "with string output"() {
        def input = "6 3 1 6 5 -2 4"
        def output = "2 1 -2 3 -2"

        builder.input = input
        def permutation = builder.createPermutation()
        def result = permutation.calculatePairs()

        expect:
        assert result.toOutputString() == output
    }

    def "it depends on the sorting of the array"() {
        when:
        def input = [5, 4, -2, 3, -5, 1]
        def permutation = builder.createPermutation(input)
        def solution = permutation.calculatePairs()
        then:
        solution.size() == 3
        solution[0].asList() == [-2, 1]
        solution[1].asList() == [-2, 3]
        solution[2].asList() == [4, -5]
    }

    def "it has to work with longer integer arrays"() {
        when:
        def input = [6, -2, 3, -5, 1, 4, 6]
        def permutation = builder.createPermutation(input)
        def solution = permutation.calculatePairs()

        then:
        solution[0].asList() == [-5, 4]
        solution[1].asList() == [-5, 6]
        solution[2].asList() == [-2, 1]
        solution[3].asList() == [-2, 3]
        solution.size() == 4
    }

    def "it has with whole left and right search"() {
        when:
        def input = [6, 1, 6, -2, 3, -5, -4]
        def permutation = builder.createPermutation(input)
        def solution = permutation.calculatePairs()

        then:
        solution[0].asList() == [-2, 3]
        solution[1].asList() == [1, -2]
        solution[2].asList() == [3, -4]
        solution[3].asList() == [6, -5]
        solution.size() == 4
    }

}

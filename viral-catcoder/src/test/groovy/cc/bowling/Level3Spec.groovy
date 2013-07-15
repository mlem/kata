package cc.bowling

import spock.lang.Ignore
import spock.lang.Specification

class Level3Spec extends Specification {

    def "first sample"() {
        def input = "8 0 3 1 6 5 -2 4 7 1 2 -2 5"
        def builder = new Builder(input: input)
        def solution = builder.createInversion()
        def invertedPermutation = solution.invertPermutation(builder.createPermutation())
        def result = invertedPermutation.calculatePoints()

        expect:
        assert result == 2
    }

    def "second sample"() {
        def input = "8 0 3 1 6 5 -2 4 7 3 1 -2 5"
        def builder = new Builder(input: input)
        def solution = builder.createInversion()
        def invertedPermutation = solution.invertPermutation(builder.createPermutation())
        def result = invertedPermutation.calculatePoints()

        expect:
        assert result == 4
    }


}

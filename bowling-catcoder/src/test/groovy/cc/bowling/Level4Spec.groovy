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

}

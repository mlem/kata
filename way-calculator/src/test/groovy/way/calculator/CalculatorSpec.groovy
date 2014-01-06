package way.calculator

import spock.lang.Specification


/**
 * There are places and streets. Streets are only between places.
 * Each place CAN be connected with an other place by a street.
 * Each place CAN have as many streets as you want.
 * There can be more streets between two places.
 *
 * Each street has a length in km
 * Each street has a tempo limit in km/h
 *
 * Write a program to calculate
 *  a.) the shortes way from place A to B
 *  b.) the fastest way from place A to B
 */
class CalculatorSpec extends Specification {

    def "place a and b with one street"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')

        a.connect(to: b, length: 10, name: streetName)
        expect:
        a.calculateShortestPathTo(b) == [streetName]
        where:
        streetName << ['ab', 'bc']
    }

    def "place a and b with more streets"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')

        a.connect(to: b, length: 8, name: 'ab-short')
        a.connect(to: b, length: 10, name: 'ab-long')
        expect:
        a.calculateShortestPathTo(b) == ['ab-short']
    }

    def "distinguish between different paths"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')
        def c = new Place(name: 'c')

        a.connect(to: b, length: 8, name: 'ab')
        a.connect(to: c, length: 10, name: 'ac')
        expect:
        a.calculateShortestPathTo(b) == ['ab']
        a.calculateShortestPathTo(c) == ['ac']
    }

    def "are those places connected?"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')

        expect:
        a.calculateShortestPathTo(b) == []
    }

    def "does a have a path to b?"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')
        def c = new Place(name: 'c')

        when:
        a.connect(to: b)

        then:
        a.hasPathTo(b)
        !a.hasPathTo(c)
    }

    def "place a has a path to c"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')
        def c = new Place(name: 'c')

        when:
        a.connect(to: b, length: 8, name: 'ab')
        b.connect(to: c, length: 10, name: 'bc')

        then:
        a.hasPathTo(c)
    }

    def "place a has no path to c"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')
        def c = new Place(name: 'c')

        when:
        a.connect(to: b)
        b.connect(to: a)

        then:
        !a.hasPathTo(c)
    }

    def "place a and c are connected via b"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')
        def c = new Place(name: 'c')

        a.connect(to: b, length: 8, name: 'ab')
        b.connect(to: c, length: 10, name: 'bc')
        expect:
        a.calculateShortestPathTo(c) == ['ab', 'bc']
    }

    def "distance from a to b is 1, even with two connections"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')
        def c = new Place(name: 'c')

        a.connect(to: b, length: 1, name: 'ab1')
        a.connect(to: b, length: 2, name: 'ab2')
        a.connect(to: c, length: 2, name: 'ac')
        c.connect(to: b, length: 1, name: 'cb')
        expect:
        a.calculatePathsTo(b) == [
                [[to: b, length: 1, name: 'ab1']],
                [[to: b, length: 2, name: 'ab2']],
                [[to: c, length: 2, name: 'ac'], [to: b, length: 1, name: 'cb']]
        ]
    }

    def "there are two paths from a to e"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')
        def c = new Place(name: 'c')
        def d = new Place(name: 'd')
        def e = new Place(name: 'e')

        a.connect(to: b, length: 1, name: 'ab')
        b.connect(to: c, length: 1, name: 'bc')
        b.connect(to: d, length: 2, name: 'bd')
        c.connect(to: e, length: 1, name: 'ce')
        d.connect(to: e, length: 1, name: 'de')
        expect:
        a.calculatePathsTo(e) == [
                [[to: b, length: 1, name: 'ab'], [to: c, length: 1, name: 'bc'], [to: e, length: 1, name: 'ce']],
                [[to: b, length: 1, name: 'ab'], [to: d, length: 2, name: 'bd'], [to: e, length: 1, name: 'de']]
        ]
    }

    def "calculate shortest path from a to e"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')
        def c = new Place(name: 'c')
        def d = new Place(name: 'd')
        def e = new Place(name: 'e')

        a.connect(to: b, length: 1, name: 'ab')
        b.connect(to: c, length: 1, name: 'bc')
        b.connect(to: d, length: 2, name: 'bd')
        c.connect(to: e, length: 3, name: 'ce')
        d.connect(to: e, length: 1, name: 'de')
        expect:
        a.calculateShortestPathTo(e) == ['ab', 'bd', 'de']
    }

    def "calculate a very complex one"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')
        def c = new Place(name: 'c')
        def d = new Place(name: 'd')
        def e = new Place(name: 'e')

        a.connect(to: b, length: 1, name: 'ab')
        b.connect(to: c, length: 1, name: 'bc')
        c.connect(to: d, length: 1, name: 'cd')
        a.connect(to: e, length: 1, name: 'ae')
        e.connect(to: d, length: 1, name: 'ed')
        expect:
        a.calculateShortestPathTo(d) == ['ae', 'ed']
    }


    def "calculate shortest path when there are many paths"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')
        def c = new Place(name: 'c')
        def d = new Place(name: 'd')
        def e = new Place(name: 'e')

        a.connect(to: b, length: 1, name: 'ab1')
        a.connect(to: b, length: 2, name: 'ab2')
        a.connect(to: b, length: 3, name: 'ab3')
        b.connect(to: c, length: 2, name: 'bc')
        b.connect(to: d, length: 2, name: 'bd2')
        b.connect(to: d, length: 1, name: 'bd1')
        c.connect(to: e, length: 3, name: 'ce3')
        c.connect(to: e, length: 1, name: 'ce1')
        d.connect(to: e, length: 1, name: 'de')
        expect:
        a.calculateShortestPathTo(e) == ['ab1', 'bd1', 'de']
    }

    def "calculate fastest way from a to b"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')

        a.connect(to: b, length: 1, name: 'ab', tempolimit: 100)
        expect:
        a.calculateFastestPathTo(b) == ['ab']

    }


    def "calculate fastest way from a to b when there are two paths with different tempo limits"() {
        given:
        def a = new Place(name: 'a')
        def b = new Place(name: 'b')

        a.connect(to: b, length: 1, name: 'ab100', tempolimit: 100)
        a.connect(to: b, length: 1, name: 'ab120', tempolimit: 120)
        expect:
        a.calculateFastestPathTo(b) == ['ab120']

    }


    class Place {

        String name
        def streets = []

        def connect(def params) {
            streets << params
        }

        def calculateShortestPathTo(def place) {
            calculateBestPathTo(place) { it.length }
        }

        private def calculateBestPathTo(destination, ratingCalculator) {
            def paths = calculatePathsTo(destination)
            def fromShortestToLongestPath = { x, y ->
                x.collect(ratingCalculator).sum() <=> y.collect(ratingCalculator).sum()
            }
            def asStreetNames = { it.name }
            paths ? paths.sort(fromShortestToLongestPath).first().collect(asStreetNames) : []
        }

        def calculateFastestPathTo(def place) {
            calculateBestPathTo(place) { it.length / it.tempolimit }
        }

        private findPossibleStreetsOnPathTo(place) {
            streets.findAll {
                it.to == place || it.to.hasPathTo(place)
            }
        }

        boolean hasPathTo(Place place, def blacklist = []) {
            blacklist << this
            streets.any { it.to == place || !blacklist.contains(it.to) && it.to.hasPathTo(place, blacklist) }
        }

        def calculatePathsTo(Place destination) {
            // all possible streets to choose towards the destination
            def streets = findPossibleStreetsOnPathTo(destination)
            def result = []
            // iterating over streets to find paths or last street
            for (def street : streets) {
                // recursive call to find the paths to the destination, minus the current street
                def paths = street.to.calculatePathsTo(destination)
                // returns current street if there are no paths yet,
                // otherwise it returnes a collection of paths plus current street
                result += paths ? paths.collect { [street] + it } : [[street]]
            }
            result
        }

        String toString() {
            "Place $name"
        }
    }


}
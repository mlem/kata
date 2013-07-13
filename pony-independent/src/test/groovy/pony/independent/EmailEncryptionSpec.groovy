package pony.independent

import spock.lang.Specification

class EmailEncryptionSpec extends Specification {
    def randomSeed = [2, 5, 3, 7, 8]
    def encrypter = new Encrypter()
    def mockedSeed = {}

    def setup() {
        encrypter.randomSeed = randomSeed
   }

    def "null is encrypted to empty string"() {
       expect:
       encrypter.encrypt(null) == ""
    }

    def "text is encrypted to unreadable text"() {
        expect:
        encrypter.encrypt("aaa") != "aaa"
    }

    def "encryption gives us random results"() {
        given:
        encrypter.randomSeed = mockedSeed
        mockedSeed.getAt(0) >> 'a'
        mockedSeed.getAt(1) >> 'b'

        expect:
        encrypter.encrypt("aaa") != encrypter.encrypt("aaa")
    }

    def "weird"() {
        expect:
        mockedSeed
    }


    static class Encrypter {
        def randomSeed
        def cursor = 0

        String encrypt(def p) {
            p ? "" + randomSeed[cursor++] : ""
        }
    }
}

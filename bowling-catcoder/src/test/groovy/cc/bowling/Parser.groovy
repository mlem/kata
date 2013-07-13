package cc.bowling

class Parser {

    def parse(String input) {
        input.split(" ").collect { it.toInteger() }
    }
}
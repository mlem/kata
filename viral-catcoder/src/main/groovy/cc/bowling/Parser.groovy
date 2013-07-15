package cc.bowling

class Parser {

    List parse(String input) {
        input.split(" ").collect { it.toInteger() } as List
    }
}
package cc.bowling

class Permutation extends ArrayList {

    Pairs calculatePairs() {
        def pairs = new Pairs(this)
        pairs.calculatePairs()
        return pairs
    }

    String toResultString() {
        this.join(" ")
    }

    String toInputString() {
        "${size()} ${toResultString()}"
    }

    int calculatePoints() {
        calculatePairs().size()
    }
}
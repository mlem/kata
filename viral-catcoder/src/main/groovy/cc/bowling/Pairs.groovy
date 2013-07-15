package cc.bowling

class Pairs extends ArrayList {

    final List permutations

    public Pairs(List permutations) {
        this.permutations = permutations
    }

    void calculatePairs() {

        def positiveGroup = permutations.findAll {
            it >= 0
        }
        def negativeGroup = permutations.findAll {
            it < 0
        }

        calculatePairsForGroups(positiveGroup, negativeGroup)
        this.sort(true) { Pair a, Pair b -> a.y <=> b.y }
        this.sort(true) { Pair a, Pair b -> a.x <=> b.x }

    }

    private void calculatePairsForGroups(List firstGroupOfIntegers, List secondGroupOfIntegers) {
        firstGroupOfIntegers.each { pos ->
            secondGroupOfIntegers.each { neg ->
                addOrientedPair(pos, neg)
            }

        }
    }

    void addOrientedPair(int x, int y) {
        if ((x.abs() - y.abs()).abs() == 1) {
            if (permutations.indexOf(x) < permutations.indexOf(y))
                this << new Pair([x, y])
            else
                this << new Pair([y, x])
        }
    }

    String toOutputString() {
        StringBuffer sb = new StringBuffer()
        sb.append(this.size())
        sb.append(" ")
        sb.append(this.collect { Pair pair ->
            pair.toPairString()
        }.join(" "))
        return sb.toString();
    }
}

package cc.bowling

class Pairs extends ArrayList {

    final List permutations
    public Pairs(ArrayList permutations) {
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
        this.sort(true) { Pair a,Pair b -> a.y <=> b.y }
        this.sort(true) { Pair a, Pair b -> a.x <=> b.x }

    }


    private void calculatePairsForGroups(List firstGroupOfIntegers, List secondGroupOfIntegers) {
        firstGroupOfIntegers.each { pos ->
            secondGroupOfIntegers.each { neg ->
                addOrientedPair(pos, neg)
            }

        }
    }




    void addOrientedPair(int firstNumber, int secondNumber) {
        if ((firstNumber.abs() - secondNumber.abs()).abs() == 1) {
            if (permutations.indexOf(firstNumber) < permutations.indexOf(secondNumber))
                this << new Pair([firstNumber, secondNumber])
            else
                this << new Pair([secondNumber, firstNumber])
        }
    }

    String toPairOutput() {
        StringBuffer sb = new StringBuffer()
        sb.append(this.size())
        sb.append(" ")
        sb.append(this.collect { Pair pair ->
            pair.toPairString()
        }.join(" "))
        return sb.toString();
    }
}

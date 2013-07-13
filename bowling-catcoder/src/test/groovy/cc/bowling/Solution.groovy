package cc.bowling

class Solution {

    List pairs = []

    List integers

    Solution calculate(ArrayList<Integer> integers) {
        def anzahlAnPermutationen = integers.remove(0)
        assert integers.size() == anzahlAnPermutationen
        this.integers = integers

        def positiveGroup = integers.findAll {
            it >= 0
        }
        def negativeGroup = integers.findAll {
            it < 0
        }

        calculatePairsForGroups(positiveGroup, negativeGroup)
        pairs.sort(true) { a, b -> a[1] <=> b[1] }
        pairs.sort(true) { a, b -> a[0] <=> b[0] }

        this
    }

    private void calculatePairsForGroups(List firstGroupOfIntegers, List secondGroupOfIntegers) {
        firstGroupOfIntegers.each { pos ->
            secondGroupOfIntegers.each { neg ->
                addOrientedPair(pos, neg)
            }

        }
    }

    Solution calculate(String input) {
        def integers = new Parser().parse(input)
        return calculate(integers)
    }


    void addOrientedPair(int firstNumber, int secondNumber) {
        if ((firstNumber.abs() - secondNumber.abs()).abs() == 1) {
            if(integers.indexOf(firstNumber) < integers.indexOf(secondNumber))
                pairs << [firstNumber, secondNumber]
            else
                pairs << [secondNumber, firstNumber]
        }
    }


    int getAnzahlPaare() {
        pairs.size()
    }

    String toPairOutput() {
        StringBuffer sb = new StringBuffer()
        sb.append(pairs.size())
        sb.append(" ")
        sb.append(pairs.collect { pair ->
            "${pair[0]} ${pair[1]}"
        }.join(" "))
        return sb.toString();
    }

    ArrayList<Integer> nextGroup(ArrayList<Integer> integers) {
        def iter = integers.iterator()
        def result = []
        if (!iter.hasNext()) {
            return []
        }
        Integer nextNum = iter.next()
        result << nextNum
        boolean startType = nextNum >= 0
        while (startType == type(nextNum) && iter.hasNext()) {
            nextNum = iter.next()
            if (startType == type(nextNum)) {
                result << nextNum
            }
        }
        return result
    }

    boolean type(int num) {
        num >= 0
    }
}
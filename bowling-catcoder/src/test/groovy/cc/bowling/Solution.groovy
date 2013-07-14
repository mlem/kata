package cc.bowling

class Solution {

    List pairs = []

    final List permutations
    ArrayList integers

    public Solution(ArrayList integers) {
        this.integers = integers
        this.permutations = filterPermutations(integers)
    }

    public Solution(String input) {
        integers = new Parser().parse(input)
        this.permutations = filterPermutations(integers)
    }

    Pairs calculatePairs() {

        def positiveGroup = permutations.findAll {
            it >= 0
        }
        def negativeGroup = permutations.findAll {
            it < 0
        }

        calculatePairsForGroups(positiveGroup, negativeGroup)
        pairs.sort(true) { a, b -> a[1] <=> b[1] }
        pairs.sort(true) { a, b -> a[0] <=> b[0] }

        new Pairs(pairs)
    }

    ArrayList<Integer> filterPermutations(ArrayList<Integer> integers) {
        List list = integers.clone()
        def anzahlAnPermutationen = list.remove(0)
        list = list.subList(0, anzahlAnPermutationen)
        assert list.size() == anzahlAnPermutationen
        list
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
                pairs << [firstNumber, secondNumber]
            else
                pairs << [secondNumber, firstNumber]
        }
    }


    int getAnzahlPaare() {
        pairs.size()
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
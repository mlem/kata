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





    ArrayList<Integer> filterPermutations(ArrayList<Integer> integers) {
        List list = integers.clone()
        def anzahlAnPermutationen = list.remove(0)
        list = list.subList(0, anzahlAnPermutationen)
        assert list.size() == anzahlAnPermutationen
        list
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

    Pairs calculatePairs() {
        def pairs = new Pairs(permutations)
        pairs.calculatePairs()
        pairs
    }
}
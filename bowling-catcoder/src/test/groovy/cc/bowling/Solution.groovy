package cc.bowling

class Solution {

    List pairs = []

    List permutations
    List<Integer> inversedPermutation

    Solution calculatePairs(ArrayList<Integer> input) {

        def positiveGroup = input.findAll {
            it >= 0
        }
        def negativeGroup = input.findAll {
            it < 0
        }

        calculatePairsForGroups(positiveGroup, negativeGroup)
        pairs.sort(true) { a, b -> a[1] <=> b[1] }
        pairs.sort(true) { a, b -> a[0] <=> b[0] }

        this
    }

    ArrayList<Integer> filterPermutations(ArrayList<Integer> integers) {
        def list = integers.clone()
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

    Solution calculate(String input) {
        def integers = new Parser().parse(input)
        permutations = filterPermutations(integers)
        return calculatePairs(permutations)
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

    Solution inversion(String input) {
        def integers = new Parser().parse(input)
        permutations = filterPermutations(integers)
        //def pairs = calculatePairs(permutations)
        def inversions = filterInversions(integers)

        inversedPermutation = permutations
        inversions.each { Inversion inversion ->
            inversedPermutation = invert(inversedPermutation, inversion)
        }
        this

    }

    List invert(ArrayList<Integer> integers, Inversion inversion) {
        if (integers[inversion.firstIndex] + integers[inversion.secondIndex] == 1) {
            invert(integers, inversion.firstIndex, inversion.secondIndex - 1)
        } else {
            invert(integers, inversion.firstIndex + 1, inversion.secondIndex)
        }
    }

    List invert(ArrayList<Integer> integers, int firstIndex, int secondIndex) {
        def list = integers[firstIndex..secondIndex]
        def reversedList = list.reverse()
        integers[firstIndex..secondIndex] = reversedList.collect { it * -1 }
        return integers
    }

    List<Inversion> filterInversions(List<Integer> integers) {
        def inversionNumbers = integers[integers[0]+1..-1]
        def inversions = []
        for (int i = 0; i < inversionNumbers.size(); i = i + 4) {
            inversions << new Inversion(
                    firstNumber: inversionNumbers[i],
                    firstIndex: inversionNumbers[i + 1],
                    secondNumber: inversionNumbers[i + 2],
                    secondIndex: inversionNumbers[i + 3]
            )
        }
        return inversions
    }

    String toInversionOutput() {
        inversedPermutation.join(" ")
    }

    int calculatePunktzahl(String input) {
        def solution = new Solution()
        solution.permutations = inversion(input).inversedPermutation
        List result = solution.calculatePairs(inversedPermutation).pairs
        return result.size();  //To change body of created methods use File | Settings | File Templates.
    }
}
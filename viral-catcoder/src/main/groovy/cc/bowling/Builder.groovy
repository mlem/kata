package cc.bowling

class Builder {

    String input
    Parser parser = new Parser()

    Permutation createPermutation() {
        def integers = parser.parse(input)
        def permutation = new Permutation()
        permutation.addAll(filterPermutations(integers))
        return permutation;  //To change body of created methods use File | Settings | File Templates.
    }
    Permutation createPermutation(List integers) {
        def permutation = new Permutation()
        permutation.addAll(filterPermutations(integers))
        return permutation;  //To change body of created methods use File | Settings | File Templates.
    }

    List<Integer> filterPermutations(ArrayList<Integer> integers) {
        List list = integers.clone()
        def permutationNumbersCount = list.remove(0)
        list = list.subList(0, permutationNumbersCount)
        assert list.size() == permutationNumbersCount
        list
    }

    Inversion createInversion() {
        def inversions = filterInversions(parser.parse(input))
        return inversions[0];  //To change body of created methods use File | Settings | File Templates.
    }



    private List<Inversion> filterInversions(List<Integer> integers) {
        if (integers[0] == integers.size() - 1) {
            return []
        }
        def inversionNumbers = integers[(integers[0] + 1)..-1]
        def inversions = []
        for (int i = 0; i <= inversionNumbers.size() - 4; i = i + 4) {
            inversions << new Inversion(
                    firstNumber: inversionNumbers[i],
                    firstIndex: inversionNumbers[i + 1],
                    secondNumber: inversionNumbers[i + 2],
                    secondIndex: inversionNumbers[i + 3]
            )
        }
        return inversions
    }

    List<Inversion> createInversionsFromPermutation() {
        def permutation = createPermutation()
        def inversions = []
        permutation.calculatePairs().each { Pair pair ->
            inversions << new Inversion(
                    firstIndex: permutation.indexOf(pair.x),
                    firstNumber:  pair.x,
                    secondIndex: permutation.indexOf(pair.y),
                    secondNumber: pair.y)
        }
        inversions
    }
}

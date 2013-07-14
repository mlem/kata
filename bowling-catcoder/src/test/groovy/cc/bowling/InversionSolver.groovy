package cc.bowling

class InversionSolver extends Solution {

    List<Inversion> inversions

    public InversionSolver(ArrayList integers) {
        super(integers)
        inversions = filterInversions(integers)
    }

    public InversionSolver(String input) {
        super(input)
        inversions = filterInversions(integers)
    }



    InvertedResult inversion() {
        def inversions = filterInversions(integers)

        inversions.each { Inversion inversion ->
            invert(inversion)
        }
        new InvertedResult(permutations)

    }

    void invert(Inversion inversion) {
        if (permutations[inversion.firstIndex] + permutations[inversion.secondIndex] == 1) {
            invert(inversion.firstIndex, inversion.secondIndex - 1)
        } else {
            invert(inversion.firstIndex + 1, inversion.secondIndex)
        }
    }

    void invert(int firstIndex, int secondIndex) {
        def list = permutations[firstIndex..secondIndex]
        def reversedList = list.reverse()
        permutations[firstIndex..secondIndex] = reversedList.collect { it * -1 }
    }

    List<Inversion> filterInversions(List<Integer> integers) {
        if(integers[0] == integers.size()+1) {
            return []
        }
        def inversionNumbers = integers[(integers[0]+1)..-1]
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


    int calculatePunktzahl() {
        inversion()
        return calculatePairs().size();  //To change body of created methods use File | Settings | File Templates.
    }


}

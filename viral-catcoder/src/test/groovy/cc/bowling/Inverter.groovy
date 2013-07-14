package cc.bowling

class Inverter {

    InvertedPermutation invertInput(String input) {
        Permutation permutation = new Permutation(input)
        def inversions = filterInversions(permutation.input)
        inversions[0].invertPermutation(permutation)

    }

    private List<Inversion> filterInversions(List<Integer> integers) {
        if(integers[0] == integers.size()-1) {
            return []
        }
        def inversionNumbers = integers[(integers[0]+1)..-1]
        def inversions = []
        for (int i = 0; i <= inversionNumbers.size()-4; i = i + 4) {
            inversions << new Inversion(
                    firstNumber: inversionNumbers[i],
                    firstIndex: inversionNumbers[i + 1],
                    secondNumber: inversionNumbers[i + 2],
                    secondIndex: inversionNumbers[i + 3]
            )
        }
        return inversions
    }


    int calculatePunktzahl(String input) {
        new Permutation(invertInput(input).toInputString()).getPairsAnzahl()
    }


}

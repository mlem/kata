package cc.bowling

class Inversion {
    int firstNumber
    int firstIndex
    int secondNumber
    int secondIndex


    InvertedPermutation invertPermutation(Permutation permutation) {
        if (permutation[firstIndex] + permutation[secondIndex] == 1) {
            invert(permutation, firstIndex, secondIndex - 1)
        } else {
            invert(permutation, firstIndex + 1, secondIndex)
        }
    }

    private Permutation invert(Permutation permutation, int firstIndex, int secondIndex) {
        InvertedPermutation tmpPermutations = new InvertedPermutation(permutation.input)
        def list = tmpPermutations[firstIndex..secondIndex]
        def reversedList = list.reverse()
        tmpPermutations[firstIndex..secondIndex] = reversedList.collect { it * -1 }
        tmpPermutations
    }

    String toOutputString() {
        "$firstNumber $firstIndex $secondNumber $secondIndex"
    }
}

package cc.bowling

class Inversion {
    int firstNumber
    int firstIndex
    int secondNumber
    int secondIndex


    Permutation invertPermutation(Permutation permutation) {
        if (permutation[firstIndex] + permutation[secondIndex] == 1) {
            invert(permutation, firstIndex, secondIndex - 1)
        } else {
            invert(permutation, firstIndex + 1, secondIndex)
        }
    }

    private Permutation invert(Permutation permutation, int firstIndex, int secondIndex) {
        def list = permutation[firstIndex..secondIndex]
        def reversedList = list.reverse()
        permutation[firstIndex..secondIndex] = reversedList.collect { it * -1 }
        permutation
    }

}

package cc.bowling

class ViralChecker {
    Builder builder

    def calculateMinimumInversions(String input) {
        builder = new Builder(input: input)
        return calculateMinimumInversions(0)
    }

    def calculateMinimumInversions(int counter) {
        Inversion currentInversion = findInversionWithMostPoints()
        if (currentInversion == null) {
            return counter
        }
        Permutation result = currentInversion.invertPermutation(builder.createPermutation())
        builder.input = result.toInputString()
        return calculateMinimumInversions(++counter)

    }

    Inversion findInversionWithMostPoints() {
        def inversions = builder.createInversionsFromPermutation()
        inversions.max { Inversion inversion ->
            def invertedPermutation = inversion.invertPermutation(builder.createPermutation())
            invertedPermutation.calculatePoints()
        }
    }
}

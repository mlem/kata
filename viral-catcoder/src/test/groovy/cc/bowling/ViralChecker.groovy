package cc.bowling

class ViralChecker {
    def calculateMinimumInversions(String input) {
        return calculateMinimumInversions(input, 0)
    }

    def calculateMinimumInversions(String input, int counter) {
        Permutation solution = new Permutation(input)
        Inversion currentInversion = findInversionWithMostPoints(input)
        if(currentInversion == null) {
            return counter
        }
        InvertedPermutation result =currentInversion.invertPermutation(solution)
        def newInputString = "${result.size()} ${result.toResultString()}"
        //println newInputString
        return calculateMinimumInversions(newInputString, ++counter)

    }

    Inversion findInversionWithMostPoints(String input) {
        Permutation permutation = new Permutation(input)
        def inversions = []
        permutation.pairs.each { Pair pair ->
            inversions << new Inversion(
                    firstIndex: permutation.indexOf(pair.x),
                    firstNumber:  pair.x,
                    secondIndex: permutation.indexOf(pair.y),
                    secondNumber: pair.y)
        }
        inversions.max {
            def solver = new Inverter()
            solver.calculatePunktzahl(input+ " " + it.toOutputString())
        }
    }
}

package cc.bowling

class ViralChecker {
    def calculateMinimumInversions(String input) {
        return calculateMinimumInversions(input, 0)
    }

    def calculateMinimumInversions(String input, int counter) {
        InversionSolver solution = new InversionSolver(input)
        Inversion currentInversion = findInversionWithMostPoints(input)
        if(currentInversion == null) {
            return counter
        }
        InvertedResult result =solution.invert(currentInversion)
        def newInputString = "${result.size()} ${result.toInversionOutput()}"
        println newInputString
        return calculateMinimumInversions(newInputString, ++counter)

    }

    Inversion findInversionWithMostPoints(String input) {
        InversionSolver solution = new InversionSolver(input)
        def pairs = solution.calculatePairs()
        def inversions = []
        pairs.each { Pair pair ->
            inversions << new Inversion(
                    firstIndex: solution.permutations.indexOf(pair.x),
                    firstNumber:  pair.x,
                    secondIndex: solution.permutations.indexOf(pair.y),
                    secondNumber: pair.y)
        }
        inversions.max {
            def solver = new InversionSolver(input+ " " + it.toOutputString())
            solver.calculatePunktzahl()
        }
    }
}

package cc.bowling

class ViralChecker {
    def calculateMinimumInversions(String input) {
        return calculateMinimumInversions(input, 0)
    }

    def calculateMinimumInversions(String input, int counter) {
        Solution solution = new InversionSolver(input)
        def pairs = new Pairs(solution.permutations)
        if(pairs.size() == 0) {
            return counter
        }
        def inversions = []
        pairs.each  { Pair pair ->
            inversions << new Inversion(firstIndex: solution.permutations.indexOf(pair.x), secondIndex: solution.permutations.indexOf(pair.y))
        }
        int currentMaxPoints = 0
        InversionSolver currentSolver = null
        for(Inversion inv in inversions) {
            def solver = new InversionSolver(input + inv.toOutputString())
            int punkte = solver.calculatePunktzahl()
            if(punkte > currentMaxPoints) {
                currentSolver = solver
                currentMaxPoints = punkte
            }

        }
        def result = currentSolver.inversion()
        return calculateMinimumInversions(result.toInversionOutput(), counter++)

    }
}

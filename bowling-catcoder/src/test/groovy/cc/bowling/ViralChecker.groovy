package cc.bowling

class ViralChecker {
    def calculateMinimumInversions(String input) {
        return calculateMinimumInversions(input, 0)
    }

    def calculateMinimumInversions(String input, int counter) {
        Solution solution = new InversionSolver(input)
        def pairs = solution.calculatePairs()
        if(pairs.size() == 0) {
            return counter
        }
        def inversions = []
        pairs.each  { pair ->
            inversions << new Inversion(firstIndex: solution.permutations.indexOf(pair[0]), secondIndex: solution.permutations.indexOf(pair[1]))
        }
        int currentMaxPoints = 0
        InversionSolver currentSolver = null
        for(Inversion inv in inversions) {
            def solver = new InversionSolver(input + " 0 ${inv.firstIndex} 0 ${inv.secondIndex}")
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

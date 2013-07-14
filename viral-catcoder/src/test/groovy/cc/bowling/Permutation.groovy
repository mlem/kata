package cc.bowling

class Permutation extends ArrayList {

    final List input
    final Pairs pairs

    public Permutation(List integers) {
        this.input = integers
        this.addAll(filterPermutations(integers))
        pairs = new Pairs(this)
        pairs.calculatePairs()
    }

    public Permutation(String input) {
        this(new Parser().parse(input))
    }

    List<Integer> filterPermutations(ArrayList<Integer> integers) {
        List list = integers.clone()
        def anzahlAnPermutationen = list.remove(0)
        list = list.subList(0, anzahlAnPermutationen)
        assert list.size() == anzahlAnPermutationen
        list
    }

    int getPairsAnzahl() {
        pairs.size()
    }

    String toResultString() {
        this.join(" ")
    }

    String toInputString() {
        "${size()} ${toResultString()}"
    }

}
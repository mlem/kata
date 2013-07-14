package cc.bowling

class InvertedResult extends ArrayList {

    public InvertedResult(ArrayList list) {
        this.addAll(list)
    }

    String toInversionOutput() {
        this.join(" ")
    }
}

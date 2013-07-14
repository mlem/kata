package cc.bowling

class Pairs extends ArrayList {

    public Pairs(ArrayList pairs) {
        this.addAll(pairs.collect { new Pair(it) } )
    }

    String toPairOutput() {
        StringBuffer sb = new StringBuffer()
        sb.append(this.size())
        sb.append(" ")
        sb.append(this.collect { Pair pair ->
            pair.toPairString()
        }.join(" "))
        return sb.toString();
    }
}

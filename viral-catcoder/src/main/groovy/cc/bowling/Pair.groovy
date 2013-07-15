package cc.bowling

class Pair {

    int x
    int y

    public Pair(ArrayList pair) {
        x = pair[0]
        y = pair[1]
    }

    List asList(){
        [x, y]
    }

    String toPairString() {
        "$x $y"
    }


}

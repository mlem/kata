package cc.bowling

class Solution {

    List pairs = []

    Solution calculate(ArrayList<Integer> integers) {
        def anzahlAnPermutationen = integers.remove(0)
        assert integers.size() == anzahlAnPermutationen

        def positiveIntegers = integers.findAll {
            it >= 0
        }

        def negativeIntegers = integers.findAll {
            it < 0
        }
        positiveIntegers.each { pos ->
            negativeIntegers.each { neg ->
                addOrientedPair(pos, neg)
            }

        }

        pairs.sort(true) { a, b -> a[0] <=> b[0] }

        this
    }

    Solution calculate(String input) {
        def integers = new Parser().parse(input)
        return calculate(integers)
    }


    void addOrientedPair(int pos, int neg) {
        if ((pos - (neg * -1)).abs() == 1) {
            pairs << [pos, neg]
        }
    }


    int getAnzahlPaare() {
        pairs.size()
    }

    String toOutput() {
        StringBuffer sb = new StringBuffer()
        sb.append(pairs.size())
        sb.append(" ")
        sb.append(pairs.collect { pair ->
            "${pair[0]} ${pair[1]}"
        }.join(" "))
        return sb.toString();
    }
}
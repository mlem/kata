package cc.bowling

import spock.lang.Specification

class Level2AcceptanceTest extends Specification {

    def "first input"() {
        def input = "8 0 3 1 6 5 -2 4 7 1 2 -2 5"
        def output = "0 3 1 2 -5 -6 4 7"

        def builder = new Builder(input: input)
        def solution = builder.createInversion()
        def result = solution.invertPermutation(builder.createPermutation())

        expect:
        assert result.toResultString() == output
    }

    def "second input"() {
        def input = "8 0 3 1 6 5 -2 4 7 3 1 -2 5"
        def output = "0 -5 -6 -1 -3 -2 4 7"

        def builder = new Builder(input: input)
        def solution = builder.createInversion()
        def result = solution.invertPermutation(builder.createPermutation())

        expect:
        assert result.toResultString() == output
    }

    def "third input"() {
        def input = "193 125 133 134 135 136 -52 -51 -50 -49 -48 -47 -46 -45 66 67 68 69 70 71 -38 -37 -36 -35 -34 -33 -32 -31 -30 -29 -132 -131 -130 -193 -192 -191 -190 -189 -188 -187 -186 -185 -184 -183 -182 -181 -180 -179 -178 -177 -176 -175 -174 -173 -172 -171 -170 -169 -77 -76 -75 -74 -73 -72 18 19 20 21 22 23 24 25 26 27 28 -164 -163 -65 -64 -63 -62 -61 -60 -59 -58 -57 -56 -55 -54 -53 39 40 41 42 43 44 159 160 161 162 -17 -16 -15 -14 -13 -12 -11 -10 -9 -8 -7 -6 -5 -4 -3 -2 -1 -168 -167 -166 -165 126 127 128 129 86 87 88 89 90 91 92 93 94 95 96 -124 -123 -122 -121 -120 -119 -118 -117 -116 -115 -114 -113 -112 -111 -110 -109 -108 -107 -106 -105 -104 -103 -102 -101 -100 -99 -98 -97 153 154 155 156 157 158 -148 -147 -146 -145 -144 -143 -142 -141 -140 -139 -138 -137 -85 -84 -83 -82 -81 -80 -79 -78 -152 -151 -150 -149 -45 12 44 94"
        def output = "125 133 134 135 136 -52 -51 -50 -49 -48 -47 -46 -45 -44 -43 -42 -41 -40 -39 53 54 55 56 57 58 59 60 61 62 63 64 65 163 164 -28 -27 -26 -25 -24 -23 -22 -21 -20 -19 -18 72 73 74 75 76 77 169 170 171 172 173 174 175 176 177 178 179 180 181 182 183 184 185 186 187 188 189 190 191 192 193 130 131 132 29 30 31 32 33 34 35 36 37 38 -71 -70 -69 -68 -67 -66 159 160 161 162 -17 -16 -15 -14 -13 -12 -11 -10 -9 -8 -7 -6 -5 -4 -3 -2 -1 -168 -167 -166 -165 126 127 128 129 86 87 88 89 90 91 92 93 94 95 96 -124 -123 -122 -121 -120 -119 -118 -117 -116 -115 -114 -113 -112 -111 -110 -109 -108 -107 -106 -105 -104 -103 -102 -101 -100 -99 -98 -97 153 154 155 156 157 158 -148 -147 -146 -145 -144 -143 -142 -141 -140 -139 -138 -137 -85 -84 -83 -82 -81 -80 -79 -78 -152 -151 -150 -149"

        def builder = new Builder(input: input)
        def solution = builder.createInversion()
        def result = solution.invertPermutation(builder.createPermutation())

        expect:
        assert result.toResultString() == output
    }
}

package kata

import static java.util.regex.Pattern.quote

class StringCalculator {
    private String splitterRegex = /,|\n/

    private int upperBound = 1000

    int run(String input) {
        Calculator calc = CalculatorFactory.create(input);
        calc.sum(input);
    }

}

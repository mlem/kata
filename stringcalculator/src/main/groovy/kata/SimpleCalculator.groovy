package kata

import java.util.regex.Pattern

/**
 * Created with IntelliJ IDEA.
 * User: BeoXTC
 * Date: 04.11.12
 * Time: 00:36
 * To change this template use File | Settings | File Templates.
 */
class SimpleCalculator implements Calculator {

    private def simpleDelimiterRegex = /,|\n/
    @Override
    int sum(String input) {
        return input.empty ? 0 : sumOfAllInteger(input)
    }

    int sumOfAllInteger(String input) {
        input.trim().split(simpleDelimiterRegex)*.toInteger().sum (ignoringValuesOverOneThousend)
    }

    def ignoringValuesOverOneThousend = { number ->
        exceptionOnNegatives(number)
        if(number <= 1000)
            return number
        return 0
    }
    
    def exceptionOnNegatives (int number) {        
        if(number < 0) {
            throw new IllegalArgumentException()
        }
    }
    
    void appendToDelimiterRegex(String delimiter) {
        simpleDelimiterRegex += "|${Pattern.quote(delimiter)}"
    }
}

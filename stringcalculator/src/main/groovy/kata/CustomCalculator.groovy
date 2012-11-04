package kata

import java.util.regex.Pattern

/**
 * Created with IntelliJ IDEA.
 * User: BeoXTC
 * Date: 04.11.12
 * Time: 00:35
 * To change this template use File | Settings | File Templates.
 */
class CustomCalculator extends SimpleCalculator {
    @Override
    int sum(String input) {
        char customDelimiter = input.charAt(2)
        simpleDelimiterRegex += "|${Pattern.quote(customDelimiter.toString())}"
        input = input.substring(4)
        return super.sum(input)  
    }
}

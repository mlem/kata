package kata

import java.util.regex.Pattern

/**
 * Created with IntelliJ IDEA.
 * User: BeoXTC
 * Date: 04.11.12
 * Time: 00:26
 * To change this template use File | Settings | File Templates.
 */
class CalculatorFactory {


    private static String userDefinedDelimiterMark = '//'
    private static String multiCharDelimiterPattern = /\[.*\]/

    static Calculator create(String input) {
        if(hasUserDefinedDelimiters(input)) {
            if(hasMultiCharDelimiters(input)) {
                return new MultiCharCalculator()
            }
            return new CustomCalculator()

        }
        return new SimpleCalculator()

    }

    private static boolean hasMultiCharDelimiters(String input) {
        Pattern.compile(multiCharDelimiterPattern).matcher(input).find()
    }

    private static boolean hasUserDefinedDelimiters(String input) {
        input.startsWith(userDefinedDelimiterMark)
    }
}

package kata

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
        if(userDefinedDelimitersOn(input)) {
            if(hasMultiCharDelimiters(input)) {
                return new MultiCharCalculator()
            }
            return new CustomCalculator()

        }
        return new SimpleCalculator()

    }

    private static boolean hasMultiCharDelimiters(String input) {
        input.matches(multiCharDelimiterPattern)
    }

    private static boolean userDefinedDelimitersOn(String input) {
        input.startsWith(userDefinedDelimiterMark)
    }
}

package kata

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
        return input.empty ? 0 : sumOfEachInteger(input)
    }

    int sumOfEachInteger(String input) {
        input.split(simpleDelimiterRegex).toList().each {it.toInteger()}.sum()
    }


}

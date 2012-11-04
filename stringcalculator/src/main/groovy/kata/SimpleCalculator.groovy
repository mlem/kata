package kata

/**
 * Created with IntelliJ IDEA.
 * User: BeoXTC
 * Date: 04.11.12
 * Time: 00:36
 * To change this template use File | Settings | File Templates.
 */
class SimpleCalculator implements Calculator {

    protected def simpleDelimiterRegex = /,|\n/
    @Override
    int sum(String input) {
        return input.empty ? 0 : sumOfAllInteger(input)
    }

    int sumOfAllInteger(String input) {
        input.trim().split(simpleDelimiterRegex)*.toInteger().sum (ignoringValuesOverOneThousend)
    }

    def ignoringValuesOverOneThousend = {
        exceptionOnNegatives(it)
        if(it <= 1000)
            return it
        return 0
    }
    
    def exceptionOnNegatives = {        
        if(it < 0) {
            throw new IllegalArgumentException()
        }
    }
}

package kata

import java.util.regex.Pattern

/**
 * Created with IntelliJ IDEA.
 * User: BeoXTC
 * Date: 04.11.12
 * Time: 00:34
 * To change this template use File | Settings | File Templates.
 */
class MultiCharCalculator extends SimpleCalculator {
    private String input;

    @Override
    int sum(String input) {
        this.input = input;
        
        userDefinedDelimiters.each {simpleDelimiterRegex += "|${Pattern.quote(it)}"}
        input = input.substring(input.indexOf("\n"))
        super.sum(input)
    }
    
    private Iterable<String> getUserDefinedDelimiters() {
        def delimiter = input.substring(3, input.indexOf('\n')-1)
        delimiter.split(/\]\[/).collect()
    }
}

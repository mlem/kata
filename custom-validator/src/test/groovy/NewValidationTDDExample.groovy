import spock.lang.Ignore
import spock.lang.Specification

class NewValidationTDDExample extends Specification {

    Validator validator
    def input

    def notNegativeRule(fieldName) {
        [
                key: fieldName,
                predicate: { input, key -> input.containsKey(key) && input[key] < 0 },
                violation: 'negative'
        ]
    }


    def setup() {
        validator = new Validator();
        input = [:]
    }

    class Validator {
        def rules

        def validate(def input) {
            def violations = [:]
            if (input) {
                rules.each {
                    if (it.predicate(input, it.key))
                        violations[it.key] = it.violation
                }
            }
            return violations
        }
    }

    def "when empty input, then no violations"() {
        when:
        def violations = validator.validate(input)
        then:
        no violations
    }

    def "when negative input, then negative value violation"() {
        given:
        validator.rules = [notNegativeRule('field')]
        input.field = -1
        when:
        def violations = validator.validate(input)
        then:
        violations.field == 'negative'
    }

    def "when zero as input, then no negative value violation"() {
        given:
        validator.rules =  [notNegativeRule('field')]
        input.field = 0
        when:
        def violations = validator.validate(input)
        then:
        no violations
    }

    def "when negative as input in field2, then no negative value violation"() {
        given:
        validator.rules = [notNegativeRule('field2')]
        input.field2 = -1
        when:
        def violations = validator.validate(input)
        then:
        violations.field2 == 'negative'
    }

    def "if the field is present, there is no violation"() {
        given:
        validator.rules = [required('field')]
        input.field = 'present'
        when:
        def violations = validator.validate(input)
        then:
        no violations
    }
    def "if the field is not present, there is required violation"() {
        given:
        validator.rules = [required('field2')]
        when:
        def violations = validator.validate(input)
        then:
        violations.field2 == 'required'
    }

    def required(String fieldName) {
        [
                key: fieldName,
                predicate: { input, key -> !input.containsKey(key) || !input[key] },
                violation: 'required'
        ]
    }

    def no(violations) {
        violations.isEmpty()
    }
}

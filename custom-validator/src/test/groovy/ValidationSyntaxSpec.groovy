import spock.lang.Ignore
import spock.lang.Specification


class ValidationSyntaxSpec extends Specification {
    def validator = new Validator()

    def "validateable component gives us a no violations result on valid input"() {
        Validateable input = GroovyMock()
        when:
        def violations = validator.validates(input)
        then:
        // 'no' can be injected with groovy meta class thingy  or static import
        no violations
    }

    def "validateable component gives us a violation result (negativ number) on negativ numerical input"() {
        Validateable input = new Validateable() {
            int age = -1

            Rules rules = new Rules(["age": [new NotNegativeRule()]])
        }
        when:
        def violations = validator.validates(input)
        then:
        // 'no' can be injected with groovy meta class thingy  or static import
        one violations
        violations["age"] == 'negative'
    }

    def "negative rule on another field"() {
        Validateable input = new Validateable() {
            int field = -1

            Rules rules = new Rules(["field": [new NotNegativeRule()]])
        }
        when:
        def violations = validator.validates(input)

        then:
        violations["field"] == 'negative'
    }

    @Ignore
    def "multiple violations on the same field"() {
         expect:
        false
    }

    boolean no(violations) {
        violations.isEmpty()
    }

    boolean one(violations) {
        violations.size() == 1
    }



    class Validator {
        def validates(input) {
            def map = [:]
            input.rules?.getRulesForField("age").each {
                if(!it.passes(input.age)) {
                    map["age"] = it.violation
                }
            }
            return map
        }
    }

    interface Validateable {

        Rules getRules()
    }

    class Rules {

        def rules

        Rules(Map rules) {

            this.rules = rules
        }

        List getRulesForField(fieldname) {
            rules[fieldname]
        }

    }

    interface Rule {
        boolean passes(value)

        String getViolation()
    }

    class Violation{

    }


    class NotNegativeRule implements Rule {
        @Override
        boolean passes(value) {
            return false  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        String getViolation() {
            return 'negative'
        }
    }

}
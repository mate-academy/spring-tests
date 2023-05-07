package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class EmailValidatorTest {
    private static EmailValidator emailValidator;
    @Mock
    private static ConstraintValidatorContext context;

    @BeforeAll
    static void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_correctEmail_oK() {
        String email = "user@gmail.com";
        Assertions.assertTrue(emailValidator.isValid(email, context), "Email is correct: " + email);
    }

    @Test
    void isValid_correctEmailWithSubDomain_oK() {
        String email = "user@sub.domain.com";
        Assertions.assertTrue(emailValidator.isValid(email, context), "Email is correct: " + email);
    }

    @Test
    void isValid_nullEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null, context));
    }

    @Test
    void isValid_withMultiplySymbolsAt_notOk() {
        Assertions.assertFalse(emailValidator.isValid("name@@gmail.com", context));
    }

    @Test
    void isValid_withoutAt_notOk() {
        String email = "usergmail.com";
        Assertions.assertFalse(emailValidator.isValid(email, context));
    }

    @Test
    void isValid_withoutPoint_notOk() {
        String email = "user@gmailcom";
        Assertions.assertFalse(emailValidator.isValid(email, context),
                "Email must be contain point");
    }

    @Test
    void isValid_domainOneSymbol_notOk() {
        String email = "user@gmail.c";
        Assertions.assertFalse(emailValidator.isValid(email, context),
                "domain length must be more than 1");
    }

    @Test
    void isValid_domainWithLastNotLetters_notOk() {
        String email = "name@gmail_.com";
        Assertions.assertFalse(emailValidator.isValid(email, context),
                "Domain must end with a letter ");
    }
}

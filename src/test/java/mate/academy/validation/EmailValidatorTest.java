package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EmailValidatorTest {
    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext context;

    @BeforeAll
    static void setup() {
        context = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_ok() {
        String validEmail = "test@test.com";
        assertTrue(emailValidator.isValid(validEmail, context),
                "Expected valid email");
    }

    @Test
    void isValid_invalidDomain_notOk() {
        String invalidEmail = "test@.com";
        assertFalse(emailValidator.isValid(invalidEmail, context),
                "Expected invalid email");
    }

    @Test
    void isValid_withoutAtSymbol_notOk() {
        String invalidEmail = "test.com";
        assertFalse(emailValidator.isValid(invalidEmail, context),
                "Expected invalid email");
    }

    @Test
    void isValid_extraSymbols_notOk() {
        String invalidEmail = "test@@test.com";
        assertFalse(emailValidator.isValid(invalidEmail, context),
                "Expected invalid email");
    }

    @Test
    void isValid_nullEmail_notOk() {
        assertFalse(emailValidator.isValid(null, context),
                "Expected invalid email");
    }
}

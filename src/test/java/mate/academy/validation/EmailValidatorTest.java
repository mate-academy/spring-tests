package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setup() {
        context = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_ok() {
        String validEmail = "test@test.com";
        Assertions.assertTrue(emailValidator.isValid(validEmail, context),
                "Expected valid email");
    }

    @Test
    void isValid_invalidDomain_notOk() {
        String invalidEmail = "test@.com";
        Assertions.assertFalse(emailValidator.isValid(invalidEmail, context),
                "Expected invalid email");
    }

    @Test
    void isValid_withoutAtSymbol_notOk() {
        String invalidEmail = "test.com";
        Assertions.assertFalse(emailValidator.isValid(invalidEmail, context),
                "Expected invalid email");
    }

    @Test
    void isValid_extraSymbols_notOk() {
        String invalidEmail = "test@@test.com";
        Assertions.assertFalse(emailValidator.isValid(invalidEmail, context),
                "Expected invalid email");
    }

    @Test
    void isValid_nullEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null, context),
                "Expected invalid email");
    }
}

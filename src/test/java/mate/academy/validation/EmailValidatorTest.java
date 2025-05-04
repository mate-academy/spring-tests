package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class EmailValidatorTest {
    private static ConstraintValidatorContext constraintValidatorContext;
    private static EmailValidator emailValidator;

    @BeforeAll
    static void beforeAll() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        String validEmail = "bchupika@mate.academy";
        boolean actual = emailValidator.isValid(validEmail, constraintValidatorContext);
        Assertions.assertTrue(actual, "true expected for email: " + validEmail);
    }

    @Test
    void isValid_invalidEmail_NotOk() {
        String invalidEmail = "--_..@123";
        boolean actual = emailValidator.isValid(invalidEmail, constraintValidatorContext);
        Assertions.assertFalse(actual, "false expected for email: " + invalidEmail);
    }
}

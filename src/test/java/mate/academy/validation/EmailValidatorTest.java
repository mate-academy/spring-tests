package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class EmailValidatorTest {
    private ConstraintValidatorContext constraintValidatorContext;
    private EmailValidator emailValidator;
    private String validEmail;
    private String invalidEmail;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
        validEmail = "bchupika@mate.academy";
        invalidEmail = "--_..@123";
    }

    @Test
    void isValid_Ok() {
        boolean actual = emailValidator.isValid(validEmail, constraintValidatorContext);
        Assertions.assertTrue(actual, "true expected for email: " + validEmail);
    }

    @Test
    void isValid_NotOk() {
        boolean actual = emailValidator.isValid(invalidEmail, constraintValidatorContext);
        Assertions.assertFalse(actual, "false expected for email: " + invalidEmail);
    }
}
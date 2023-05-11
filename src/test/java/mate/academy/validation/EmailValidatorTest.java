package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private String validEmail = "bob@i.ua";
    private String invalidEmail = "invalidEmail";
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void validate_Ok() {
        Assertions.assertTrue(emailValidator.isValid(validEmail, constraintValidatorContext));
    }

    @Test
    void validate_invalidEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(invalidEmail, constraintValidatorContext));
    }

    @Test
    void validate_nullEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    void validate_emptyEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid("", constraintValidatorContext));
    }
}

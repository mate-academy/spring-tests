package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(emailValidator.isValid(VALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_nullEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    void isValid_emptyEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid("", constraintValidatorContext));
    }

    @Test
    void isValid_invalidEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid("invalid", constraintValidatorContext));
    }
}

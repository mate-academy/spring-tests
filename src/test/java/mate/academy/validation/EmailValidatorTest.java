package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "invalidEmail";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void validate_Ok() {
        Assertions.assertTrue(emailValidator.isValid(VALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void validate_invalidEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(INVALID_EMAIL, constraintValidatorContext));
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

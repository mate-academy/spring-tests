package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmailValidatorTest {
    private static final String TEST_EMAIL = "testEmail@gmail.com";
    private static EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void validateEmail_EmailIsValid_Ok() {
        Assertions.assertTrue(emailValidator
                .isValid(TEST_EMAIL,constraintValidatorContext),
                "The actual email doesn't match the expected data");
    }

    @Test
    void validateEmail_NullEmail_NotOk() {
        Assertions.assertFalse(emailValidator
                .isValid(null,constraintValidatorContext));
    }

    @Test
    void validateEmail_NonExistingEmail_NotOk() {
        Assertions.assertFalse(emailValidator
                .isValid("wrong",constraintValidatorContext));
    }
}

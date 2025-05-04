package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String EMAIL = "bob@domain.com";
    private ConstraintValidatorContext constraintValidatorContext;
    private final EmailValidator emailValidator = new EmailValidator();

    @Test
    void isValid_ValidEmail_Ok() {
        boolean actual = emailValidator.isValid(EMAIL, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_NotValidEmail_NotOk() {
        boolean actual = emailValidator.isValid(EMAIL + ".", constraintValidatorContext);
        Assertions.assertFalse(actual);

    }

    @Test
    void isValid_NullEmail_NotOk() {
        boolean actual = emailValidator.isValid(null, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}

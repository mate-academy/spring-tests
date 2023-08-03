package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@gmail.com";
    private static final String INVALID_EMAIL = "bob";
    private static final String INVALID_EMAIL_SNAIL = "bobgmail.com";
    private static final String INVALID_EMAIL_DOT = "bob@gmailcom";
    private static final String INVALID_EMAIL_EMPTY = "";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        boolean isValid = emailValidator.isValid(VALID_EMAIL, constraintValidatorContext);
        Assertions.assertTrue(isValid);
    }

    @Test
    void isValid_invalidEmail_notOk() {
        boolean isValid = emailValidator.isValid(INVALID_EMAIL, constraintValidatorContext);
        Assertions.assertFalse(isValid);
    }

    @Test
    void isValid_invalidEmailSnail_notOk() {
        boolean isValid = emailValidator.isValid(INVALID_EMAIL_SNAIL, constraintValidatorContext);
        Assertions.assertFalse(isValid);
    }

    @Test
    void isValid_invalidEmailDot_notOk() {
        boolean isValid = emailValidator.isValid(INVALID_EMAIL_DOT, constraintValidatorContext);
        Assertions.assertFalse(isValid);
    }

    @Test
    void isValid_invalidEmailEmpty_notOk() {
        boolean isValid = emailValidator.isValid(INVALID_EMAIL_EMPTY, constraintValidatorContext);
        Assertions.assertFalse(isValid);
    }

    @Test
    void isValid_null_notOk() {
        boolean isValid = emailValidator.isValid(null, constraintValidatorContext);
        Assertions.assertFalse(isValid);
    }
}

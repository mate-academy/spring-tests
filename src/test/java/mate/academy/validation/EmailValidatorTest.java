package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "user@gmail.com";
    private static final String WRONG_EMAIL_NO_AT = "usergmail.com";
    private static final String WRONG_EMAIL_ENDS_WITH_DOT = "1user@gmail.com.";
    private static final String WRONG_EMAIL_NO_DOMAIN = "user@";
    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        assertTrue(emailValidator.isValid(VALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_WrongEmail_NotOk() {
        assertFalse(emailValidator.isValid(WRONG_EMAIL_NO_AT, constraintValidatorContext));
        assertFalse(emailValidator.isValid(WRONG_EMAIL_ENDS_WITH_DOT, constraintValidatorContext));
        assertFalse(emailValidator.isValid(WRONG_EMAIL_NO_DOMAIN, constraintValidatorContext));
    }

    @Test
    void isValid_NullEmail_NotOk() {
        assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}

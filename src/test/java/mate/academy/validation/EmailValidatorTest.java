package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailValidatorTest {
    private static final String VALID_EMAIL = "test@ukr.net";
    private static final String EMAIL_NO_SNAIL = "testukr.net";
    private static final String EMAIL_NO_DOT = "test@ukrnet";
    private static final String EMAIL_WRONG_DOMAIN = "test@ukr.s";
    private static final String INVALID_EMAIL = "@ukr.net";
    private static final String EMPTY_EMAIL = "";
    private static EmailValidator emailValidator;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void init() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_validEmail_ok() {
        assertTrue(emailValidator.isValid(VALID_EMAIL, constraintValidatorContext),
                "method should return true when email is valid");
    }

    @Test
    void isValid_notValidEmail_notOk() {
        assertFalse(emailValidator.isValid(EMAIL_NO_SNAIL, constraintValidatorContext),
                "method should return false if email doesn`t not contain snail");
        assertFalse(emailValidator.isValid(EMAIL_NO_DOT, constraintValidatorContext),
                "method should return false if email doesn`t not contain dot before domain");
        assertFalse(emailValidator.isValid(EMAIL_WRONG_DOMAIN, constraintValidatorContext),
                "method should return false if email contain wrong domain");
        assertFalse(emailValidator.isValid(INVALID_EMAIL, constraintValidatorContext),
                "method should return false if email is incorrect");
        assertFalse(emailValidator.isValid(EMPTY_EMAIL, constraintValidatorContext),
                "method should return false if email is empty");
    }

    @Test
    void isValid_nullEmail_notOk() {
        assertFalse(emailValidator.isValid(null, constraintValidatorContext),
                "method should return false if email is null");
    }
}

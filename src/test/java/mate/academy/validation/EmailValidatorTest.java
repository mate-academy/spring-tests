package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EmailValidatorTest {
    private static final String VALID_EMAIL_BOB = "bob@mail.com";
    private static final String VALID_EMAIL_ALICE = "alice@i.ua";
    private static final String VALID_EMAIL_SAM = "sam@gmail.com";
    private static final String INVALID_EMAIL_1 = "f4t24s52";
    private static final String INVALID_EMAIL_2 = "1525134522513";
    private static final String INVALID_EMAIL_3 = "alice@com.com@com";
    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        String message = "When email is correct you must return true";
        assertTrue(emailValidator.isValid(VALID_EMAIL_BOB, constraintValidatorContext), message);
        assertTrue(emailValidator.isValid(VALID_EMAIL_ALICE, constraintValidatorContext), message);
        assertTrue(emailValidator.isValid(VALID_EMAIL_SAM, constraintValidatorContext), message);
    }

    @Test
    void isValid_NotOk() {
        String message = "When email is incorrect you must return false";
        assertFalse(emailValidator.isValid(INVALID_EMAIL_1, constraintValidatorContext), message);
        assertFalse(emailValidator.isValid(INVALID_EMAIL_2, constraintValidatorContext), message);
        assertFalse(emailValidator.isValid(INVALID_EMAIL_3, constraintValidatorContext), message);
    }
}

package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String EMAIL = "bob@i.ua";
    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void beforeAll() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_ok() {
        boolean actual = emailValidator.isValid(EMAIL, constraintValidatorContext);
        assertTrue(actual);
    }

    @Test
    void isValid_withoutSnail_notOk() {
        boolean actual = emailValidator.isValid("bobi.ua", constraintValidatorContext);
        assertFalse(actual);
    }

    @Test
    void isValid_withoutTail_notOk() {
        boolean actual = emailValidator.isValid("bob@", constraintValidatorContext);
        assertFalse(actual);
    }

    @Test
    void isValid_withoutName_notOk() {
        boolean actual = emailValidator.isValid("@i.ua", constraintValidatorContext);
        assertFalse(actual);
    }

    @Test
    void isValid_withoutDot_notOk() {
        boolean actual = emailValidator.isValid("bob@iua", constraintValidatorContext);
        assertFalse(actual);
    }
}

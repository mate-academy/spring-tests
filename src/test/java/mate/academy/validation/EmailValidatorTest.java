package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String EMAIL = "bob@i.ua";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_ok() {
        boolean actual = emailValidator.isValid(EMAIL, constraintValidatorContext);
        assertTrue(actual);
    }

    @Test
    void isValid_withoutSnail_notOk() {
        String email = "bobi.ua";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        assertFalse(actual);
    }

    @Test
    void isValid_withoutDot_notOk() {
        String email = "bob@iua";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        assertFalse(actual);
    }

    @Test
    void isValid_emptyFirstPart_notOk() {
        String email = "@i.ua";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        assertFalse(actual);
    }

    @Test
    void isValid_withoutTail_notOk() {
        String email = "bob@";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        assertFalse(actual);
    }

    @Test
    void isValid_Null_notOk() {
        boolean actual = emailValidator.isValid(null, constraintValidatorContext);
        assertFalse(actual);
    }
}

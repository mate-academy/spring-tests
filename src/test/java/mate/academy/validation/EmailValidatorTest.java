package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "bob@i.";
    private static final String INVALID_EMAIL_EMPTY_PREFIX = "@i.ua";
    private static final String INVALID_EMAIL_EMPTY_TAIL = "bob@";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_ok() {
        assertTrue(emailValidator.isValid(EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        assertFalse(emailValidator.isValid(INVALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_invalidEmailEmptyPrefix() {
        assertFalse(emailValidator.isValid(INVALID_EMAIL_EMPTY_PREFIX, constraintValidatorContext));
    }

    @Test
    void isValid_invalidEmailEmptyTail() {
        assertFalse(emailValidator.isValid(INVALID_EMAIL_EMPTY_TAIL, constraintValidatorContext));
    }
}

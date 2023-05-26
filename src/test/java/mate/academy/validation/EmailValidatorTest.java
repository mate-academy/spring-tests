package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        String validEmail = "test@example.com";
        boolean isValid = emailValidator.isValid(validEmail, context);
        assertTrue(isValid);
    }

    @Test
    void isValid_NotOk() {
        String invalidEmail = "invalid.email";
        boolean isValid = emailValidator.isValid(invalidEmail, context);
        assertFalse(isValid);
    }

    @Test
    void isValid_NullEmail_NotOk() {
        boolean isValid = emailValidator.isValid(null, context);
        assertFalse(isValid);
    }
}

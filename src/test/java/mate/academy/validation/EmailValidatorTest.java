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
    void validateEmail_validEmail_Ok() {
        String validEmail = "aboba@example.com";
        assertTrue(emailValidator.isValid(validEmail, context));
    }

    @Test
    void validateEmail_invalidEmail_notOk() {
        String invalidEmail = "grihfdaw";
        assertFalse(emailValidator.isValid(invalidEmail, context));
    }
}

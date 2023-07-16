package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@mail.com";
    private static final String INVALID_EMAIL = "bob@com";

    private final ConstraintValidator<Email, String> emailValidator = new EmailValidator();
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    @Test
    void isValid_validEmail_ok() {
        assertTrue(emailValidator.isValid(VALID_EMAIL, context));
    }

    @Test
    void isValid_invalidEmail_ok() {
        assertFalse(emailValidator.isValid(INVALID_EMAIL, context));
    }

    @Test
    void isValid_emailIsNull_ok() {
        assertFalse(emailValidator.isValid(null, context));
    }
}

package mate.academy.validation;

import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@gmail.com";
    private static final String INVALID_EMAIL = "bob@bob";

    private final ConstraintValidator<Email, String> emailValidator = new EmailValidator();
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    @Test
    void isValid_validEmail_ok() {
        Assertions.assertTrue(emailValidator.isValid(VALID_EMAIL, context));
    }

    @Test
    void isValid_invalidEmail_ok() {
        Assertions.assertFalse(emailValidator.isValid(INVALID_EMAIL, context));
    }

    @Test
    void isValid_emailIsNull_ok() {
        Assertions.assertFalse(emailValidator.isValid(null, context));
    }
}


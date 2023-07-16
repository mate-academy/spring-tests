package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "bob";
    private final EmailValidator emailValidator = new EmailValidator();
    private final ConstraintValidatorContext constraintValidatorContext =
            mock(ConstraintValidatorContext.class);

    @Test
    void isValid_Ok() {
        assertTrue(
                emailValidator.isValid(VALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_NotOk() {
        assertFalse(
                emailValidator.isValid(INVALID_EMAIL, constraintValidatorContext));
    }
}

package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private final EmailValidator emailValidator = new EmailValidator();
    private final ConstraintValidatorContext constraintValidatorContext =
            Mockito.mock(ConstraintValidatorContext.class);

    @Test
    void isValid_Ok() {
        assertTrue(emailValidator.isValid("user@gmail.com", constraintValidatorContext));
        assertTrue(emailValidator.isValid("us_er@mindtitan.com", constraintValidatorContext));
        assertTrue(emailValidator.isValid("us.er@mindtitan.yahoo.net", constraintValidatorContext));
    }

    @Test
    void isValid_InvalidEmail_NotOk() {
        assertFalse(emailValidator.isValid(" user@gmail.com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("u er@gmail.com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("@gmail.com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("user.gmail.com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("us@r@gmail.com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("user@gmailcom", constraintValidatorContext));
        assertFalse(emailValidator.isValid("user@gmail..com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("user@gmail.com_ua", constraintValidatorContext));
    }

    @Test
    void isValid_NullEmail_NotOk() {
        assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}

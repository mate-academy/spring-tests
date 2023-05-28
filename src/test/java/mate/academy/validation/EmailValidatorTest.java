package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String DISALLOWED_SYMBOLS = " !#$%&'*+/=?^_`{|}~@";
    private final EmailValidator emailValidator = new EmailValidator();
    private final ConstraintValidatorContext mockedContext =
            Mockito.mock(ConstraintValidatorContext.class);

    @Test
    void isValid_validEmail_ok() {
        String correctEmail = "azAZ09.!#$%&'*+/=?^_`{|}~-@gmail.com";
        Assertions.assertTrue(emailValidator.isValid(correctEmail, mockedContext));
    }

    @Test
    void isValid_invalidEmails_ok() {
        Assertions.assertFalse(emailValidator.isValid(null, mockedContext));
        Assertions.assertFalse(emailValidator.isValid(".com", mockedContext));
        Assertions.assertFalse(emailValidator.isValid("@.com", mockedContext));
        Assertions.assertFalse(emailValidator.isValid("email@.com", mockedContext));
        Assertions.assertFalse(emailValidator.isValid("@gmail.com", mockedContext));
        Assertions.assertFalse(emailValidator.isValid("gmail.com", mockedContext));
        for (int i = 0; i < DISALLOWED_SYMBOLS.length(); i++) {
            Assertions.assertFalse(emailValidator.isValid(String.format("email@gmail%s.com",
                    DISALLOWED_SYMBOLS.charAt(i)), mockedContext));
        }
    }
}

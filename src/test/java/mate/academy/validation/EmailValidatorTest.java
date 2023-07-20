package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EmailValidatorTest {
    private static final String TEST_EMAIL_OK = "gleb1234@gmail.com";
    private ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
    private ConstraintValidator<Email, String> emailValidator = new EmailValidator();

    @Test
    void isValid_Ok() {
        boolean actual = emailValidator.isValid(TEST_EMAIL_OK, context);
        assertTrue(actual);
    }

    @Test
    void isValid_Null_Email_Not_Ok() {
        boolean actual = emailValidator.isValid(null, context);
        assertFalse(actual);
    }
}

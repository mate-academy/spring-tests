package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {
    private static EmailValidator emailValidator;
    private static String validEmail;
    private static String invalidEmail;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
        validEmail = "bob@i.ua";
        invalidEmail = "iAmInvalidEmail";
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        boolean actual = emailValidator.isValid(validEmail,constraintValidatorContext);
        assertTrue(actual);
    }

    @Test
    void isValid_NotOk() {
        boolean actual = emailValidator.isValid(invalidEmail,constraintValidatorContext);
        assertFalse(actual);
    }
}
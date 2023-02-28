package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext context;

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_validEmail_shouldReturnTrue() {
        String validEmail = "denik@gmail.com";
        Assertions.assertTrue(emailValidator.isValid(validEmail, context));
    }

    @Test
    void isValid_unValidEmail_shouldReturnFalse() {
        String unValidEmail = "d@..jgl.fffmdkkd";
        Assertions.assertFalse(emailValidator.isValid(unValidEmail, context));
    }

    @Test
    void isValid_nullEmail_shouldReturnFalse() {
        Assertions.assertFalse(emailValidator.isValid(null, context));
    }
}

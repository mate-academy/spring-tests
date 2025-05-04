package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        String validEmail = "qwerty@gmail.com";
        Assertions.assertTrue(emailValidator.isValid(validEmail, context));
    }

    @Test
    void isValid_InvalidEmail_Ok() {
        String validEmail = "qwertygmail.com";
        Assertions.assertFalse(emailValidator.isValid(validEmail, context));
    }
}

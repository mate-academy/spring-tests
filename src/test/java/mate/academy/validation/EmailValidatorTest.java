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
        String email = "bob@i.ua";
        Assertions.assertTrue(emailValidator.isValid(email, context));
    }

    @Test
    void isValid_NotValidEmail_NotOk() {
        String email = "bob-i.ua";
        Assertions.assertFalse(emailValidator.isValid(email, context));
    }

    @Test
    void isValid_NullEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(null, context));
    }
}

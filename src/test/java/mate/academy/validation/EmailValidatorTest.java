package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_ok() {
        String email = "validEmail@com.ua";
        Assertions.assertTrue(emailValidator.isValid(email, context));
    }

    @Test
    void isValid_emptyEmail_notOk() {
        String emptyEmail = "";
        Assertions.assertFalse(emailValidator.isValid(emptyEmail, context));
    }

    @Test
    void isValid_nullEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null, context));
    }

    @Test
    void isValid_incorrectEmail_notOk() {
        String email = "incorrect_email";
        Assertions.assertFalse(emailValidator.isValid(email, context));
    }
}

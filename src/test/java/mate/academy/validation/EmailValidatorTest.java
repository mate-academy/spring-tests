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
        Assertions.assertTrue(emailValidator.isValid("bob@i.ua", context));
        Assertions.assertTrue(emailValidator.isValid("bob.123@gmail.com", context));
    }

    @Test
    void isValid_NotOk() {
        Assertions.assertFalse(emailValidator.isValid("@i.ua.bob", context));
        Assertions.assertFalse(emailValidator.isValid("bob.123", context));
        Assertions.assertFalse(emailValidator.isValid("1234@gmail", context));
    }
}

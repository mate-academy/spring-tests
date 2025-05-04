package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext validatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        validatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid("bob@i.ua", validatorContext));
        Assertions.assertTrue(emailValidator.isValid("bob.123@gmail.com", validatorContext));
    }

    @Test
    void isValid_NotOk() {
        Assertions.assertFalse(emailValidator.isValid("@i.ua.bob", validatorContext));
        Assertions.assertFalse(emailValidator.isValid("bob.123", validatorContext));
        Assertions.assertFalse(emailValidator.isValid("1234@gmail", validatorContext));
    }
}

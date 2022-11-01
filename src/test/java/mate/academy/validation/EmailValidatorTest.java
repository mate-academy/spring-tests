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
    }

    @Test
    void isValid_NotOk() {
        Assertions.assertFalse(emailValidator.isValid("", context));
        Assertions.assertFalse(emailValidator.isValid("djfch", context));
        Assertions.assertFalse(emailValidator.isValid("dgch.hd@dvhjm..com", context));
    }

    @Test
    void isValid_Null() {
        Assertions.assertFalse(emailValidator.isValid(null, context));
    }
}

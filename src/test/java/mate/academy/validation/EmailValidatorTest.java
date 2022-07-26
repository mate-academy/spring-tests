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
    }

    @Test
    void isValid_notOk() {
        Assertions.assertFalse(emailValidator.isValid("ua", validatorContext));
        Assertions.assertFalse(emailValidator.isValid("ukr.net", validatorContext));
        Assertions.assertFalse(emailValidator.isValid("@ukr.net", validatorContext));
        Assertions.assertFalse(emailValidator.isValid(null, validatorContext));
    }
}

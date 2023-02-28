package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext validatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_emailIsNull_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null,validatorContext));
    }

    @Test
    void isValid_emailIsEmpty_notOk() {
        Assertions.assertFalse(emailValidator.isValid(" ",validatorContext));
    }

    @Test
    void isValid_emailDoNotMatch_notOk() {
        Assertions.assertFalse(emailValidator.isValid("BoB s@gmail.eu",validatorContext));
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(emailValidator.isValid("bob@i.ua",validatorContext));
    }
}

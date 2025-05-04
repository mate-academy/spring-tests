package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class EmailValidatorTest {
    private static final String LOGIN = "hello@i.am";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }
    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid(LOGIN, constraintValidatorContext));
    }

    @Test
    void isValid_nullLogin_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    void isValid_emptyLogin_notOk() {
        Assertions.assertFalse(emailValidator.isValid("", constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        Assertions.assertFalse(emailValidator.isValid("11333@44.09", constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("   @ . ", constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("hello@net", constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("hellonet.ua", constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("hello@net.", constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("hello@.net", constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("t@t.t", constraintValidatorContext));
    }
}

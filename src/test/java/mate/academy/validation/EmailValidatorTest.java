package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String EMAIL = "testmail@i.ua";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_invalidEmail_NotOk() {
        boolean isValid = emailValidator.isValid("email", constraintValidatorContext);
        Assertions.assertFalse(isValid);
    }

    @Test
    void isValid_invalidEmailSnail_NotOk() {
        boolean isValid = emailValidator.isValid("testmaili.ua", constraintValidatorContext);
        Assertions.assertFalse(isValid);
    }

    @Test
    void isValid_invalidEmailDot_NotOk() {
        boolean isValid = emailValidator.isValid("testmail@iua", constraintValidatorContext);
        Assertions.assertFalse(isValid);
    }

    @Test
    void isValid_invalidEmailEmpty_NotOk() {
        boolean isValid = emailValidator.isValid("", constraintValidatorContext);
        Assertions.assertFalse(isValid);
    }

    @Test
    void isValid_null_NotOk() {
        boolean isValid = emailValidator.isValid(null, constraintValidatorContext);
        Assertions.assertFalse(isValid);
    }

    @Test
    void isValid_Ok() {
        boolean isValid = emailValidator.isValid(EMAIL, constraintValidatorContext);
        Assertions.assertTrue(isValid);
    }
}

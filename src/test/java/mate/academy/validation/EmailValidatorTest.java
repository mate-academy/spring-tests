package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private final String WRIGHT_EMAIL = "bobik@g.com";
    private final String WRONG_EMAIL = "bobik.com";

    @BeforeEach
    void setup() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid(WRIGHT_EMAIL, constraintValidatorContext));
    }
    @Test
    void isValid_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(WRONG_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_NullEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}
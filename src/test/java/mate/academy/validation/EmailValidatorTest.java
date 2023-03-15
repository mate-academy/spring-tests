package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String EMAIL = "alice@gmail.com";
    private static final String WRONG_EMAIL = "bob.com";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setup() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid(EMAIL, constraintValidatorContext));
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

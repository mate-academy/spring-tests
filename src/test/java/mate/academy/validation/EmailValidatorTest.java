package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String WRITE_EMAIL = "email@gmail.com";
    private static final String WRONG_EMAIL = "email";
    private ConstraintValidatorContext constraintValidatorContext;
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_validEmail_ok() {
        Assertions.assertTrue(emailValidator.isValid(WRITE_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_notValidEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(WRONG_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_nullEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}

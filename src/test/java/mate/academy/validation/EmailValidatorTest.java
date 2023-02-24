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
    void validEmail_Ok() {
        Assertions.assertTrue(emailValidator.isValid(WRITE_EMAIL, constraintValidatorContext));
    }

    @Test
    void notValidEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(WRONG_EMAIL, constraintValidatorContext));
    }

    @Test
    void nullEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}

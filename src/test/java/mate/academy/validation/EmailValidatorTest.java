package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {
    private final static String WRITE_EMAIL = "email@gmail.com";
    private final static String WRONG_EMAIL = "email";
    private ConstraintValidatorContext constraintValidatorContext;
    EmailValidator emailValidator;
    
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
    void NullEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}
package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_ok() {
        String validEmail = "bob@i.ua";
        boolean isValid = emailValidator.isValid(validEmail, constraintValidatorContext);
        Assertions.assertTrue(isValid);
    }

    @Test
    void isValid_notOk() {
        String nullEmail = null;
        String emailWithoutDot = "bob@iua";
        String emailWithoutAt = "bobi.ua";
        String emailWithFirstAt = "@i.ua";
        String emailWithLastAt = "bob@i.ua@";
        String emailWithSpace = "bob @i.ua";
        String emailWithLastDot = "567@gmail.";
        Assertions.assertFalse(emailValidator.isValid(nullEmail, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithoutDot, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithoutAt, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithFirstAt, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithLastAt, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithSpace, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithLastDot, constraintValidatorContext));
    }
}
package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        String validEmail = "my@i.ua";
        Assertions.assertTrue(emailValidator.isValid(validEmail, constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        String emailWithoutDot = "my@iua";
        String emailWithoutAt = "myui.ua";
        String emailWithFirstAt = "@i.ua";
        String nullEmail = null;
        String emailWithSpace = "m y@i.ua";
        String emailWithLastAt = "my@i.ua@";
        Assertions.assertFalse(emailValidator.isValid(emailWithoutDot, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithoutAt, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithFirstAt, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(nullEmail, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithSpace, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithLastAt, constraintValidatorContext));
    }
}
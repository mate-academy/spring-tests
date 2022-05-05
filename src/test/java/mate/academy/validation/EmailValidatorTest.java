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
        String validEmail = "bob@i.ua";
        Assertions.assertTrue(emailValidator.isValid(validEmail, constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        String emailWithoutDot = "bob@iua";
        String emailWithoutAt = "bobi.ua";
        String emailWithFirstAt = "@i.ua";
        String nullEmail = null;
        String emailWithSpace = "bob @i.ua";
        String emailWithLastAt = "bob@i.ua@";
        Assertions.assertFalse(emailValidator.isValid(emailWithoutDot, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithoutAt, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithFirstAt, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(nullEmail, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithSpace, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(emailWithLastAt, constraintValidatorContext));
    }
}

package mate.academy.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private ConstraintValidator<Email, String> constraintValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        constraintValidator = new EmailValidator();
    }

    @Test
    void checkValidEmail_Ok() {
        String validEmail = "Max1@i.ua";
        boolean actual = constraintValidator.isValid(validEmail, constraintValidatorContext);
        boolean expected = true;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkInvalidEmail_NotOk() {
        String inValidEmail = "max";
        boolean actual = constraintValidator.isValid(inValidEmail, constraintValidatorContext);
        boolean expected = false;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkNullEmail_NotOk() {
        String nullEmail = null;
        boolean actual = constraintValidator.isValid(nullEmail, constraintValidatorContext);
        boolean expected = false;
        Assertions.assertEquals(expected, actual);
    }
}

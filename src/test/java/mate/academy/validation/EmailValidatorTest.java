package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class EmailValidatorTest {
    ConstraintValidator<Email, String> constraintValidator;
    ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        constraintValidator = new EmailValidator();
    }

    @Test
    void checkValidEmail_True() {
        String validEmail = "Max1@i.ua";
        boolean actual = constraintValidator.isValid(validEmail, constraintValidatorContext);
        boolean expected = true;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkInValidEmail_False() {
        String inValidEmail = "max";
        boolean actual = constraintValidator.isValid(inValidEmail, constraintValidatorContext);
        boolean expected = false;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkNullEmail_False() {
        String nullEmail = null;
        boolean actual = constraintValidator.isValid(nullEmail, constraintValidatorContext);
        boolean expected = false;
        Assertions.assertEquals(expected, actual);
    }
}

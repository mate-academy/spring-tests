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
        String validEmail = "valid@i.ua";
        Assertions.assertTrue(emailValidator.isValid(validEmail, constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        String nullEmail = null;
        String withoutAt = "valid.ua";
        String withFirstAt = "@i.ua";
        String withoutDot = "valid@iua";
        String withSpace = "v alid@i.ua";
        String withLastAt = "valid@i.ua@";
        Assertions.assertFalse(emailValidator.isValid(nullEmail, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(withoutAt, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(withFirstAt, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(withoutDot, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(withSpace, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(withLastAt, constraintValidatorContext));
    }
}

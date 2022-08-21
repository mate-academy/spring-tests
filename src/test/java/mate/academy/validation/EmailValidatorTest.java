package mate.academy.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private ConstraintValidator<Email, String> validator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
     void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        validator = new EmailValidator();
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(validator.isValid("user@mail.com", constraintValidatorContext));
    }

    @Test
    void isValid_incorrectEmail_notOk() {
        Assertions.assertFalse(validator.isValid("usermail.com", constraintValidatorContext));
        Assertions.assertFalse(validator.isValid("user@mail", constraintValidatorContext));
        Assertions.assertFalse(validator.isValid("usermail", constraintValidatorContext));
        Assertions.assertFalse(validator.isValid("", constraintValidatorContext));
    }
}

package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
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
        assertTrue(validator.isValid("harrington@mail.com", constraintValidatorContext));
    }

    @Test
    void isValid_incorrectEmail_notOk() {
        assertFalse(validator.isValid("harringtonmail.com", constraintValidatorContext));
        assertFalse(validator.isValid("harrington@mail", constraintValidatorContext));
        assertFalse(validator.isValid("nonsense", constraintValidatorContext));
        assertFalse(validator.isValid("", constraintValidatorContext));
    }

    @Test
    void isValid_nullEmail_notOk() {
        assertFalse(validator.isValid(null, constraintValidatorContext));
    }
}

package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
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
    void isValid_Ok() {
        assertTrue(emailValidator.isValid("bob@mate.academy", constraintValidatorContext));
    }

    @Test
    void isValid_nullEmail_notOk() {
        assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    void isValid_incorrectAtSign_notOk() {
        assertFalse(emailValidator.isValid("bob@mate.ac@ademy", constraintValidatorContext));
        assertFalse(emailValidator.isValid("@bobmate.academy", constraintValidatorContext));
        assertFalse(emailValidator.isValid("bobmate.academy@", constraintValidatorContext));
        assertFalse(emailValidator.isValid("bobmate.academy", constraintValidatorContext));
    }

    @Test
    void isValid_incorrectDot_notOk() {
        assertFalse(emailValidator.isValid("bob@mateacademy", constraintValidatorContext));
        assertFalse(emailValidator.isValid(".bob@mateacademy", constraintValidatorContext));
        assertFalse(emailValidator.isValid("bob@mateacademy.", constraintValidatorContext));
    }
}

package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_emptyEmail_NotOk() {
        assertFalse(emailValidator.isValid("", constraintValidatorContext));
    }

    @Test
    void isValid_Null_NotOk() {
        assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    void isValid_emailWithoutDogSymbol_NotOk() {
        String email = "minimal.ukr.net";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_Ok() {
        String email = "john@ukr.net";
        assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }
}

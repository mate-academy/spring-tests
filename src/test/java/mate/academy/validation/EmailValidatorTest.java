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
    void isValid_emptyEmail_notOk() {
        assertFalse(emailValidator.isValid("", constraintValidatorContext));
    }

    @Test
    void isValid_nullEmail_notOk() {
        assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    void isValid_validMail_ok() {
        String email = "usermail@gmail.com";
        assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_emailWithoutAtSymbol_notOk() {
        String email = "usermail1_gmail.com";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }
}

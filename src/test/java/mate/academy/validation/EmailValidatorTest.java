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
        final String correctEmail1 = "user@mail.com";
        final String correctEmail2 = "user1@mail.com";
        final String correctEmail3 = "user.1@mail.com";
        final String correctEmail4 = "user@mail.com.ua";
        final String correctEmail5 = "user&1@mail.com";
        assertTrue(emailValidator.isValid(correctEmail1,constraintValidatorContext));
        assertTrue(emailValidator.isValid(correctEmail2,constraintValidatorContext));
        assertTrue(emailValidator.isValid(correctEmail3,constraintValidatorContext));
        assertTrue(emailValidator.isValid(correctEmail4,constraintValidatorContext));
        assertTrue(emailValidator.isValid(correctEmail5,constraintValidatorContext));
    }

    @Test
    void isValid_Not_Ok() {
        final String incorrectEmail1 = "use@r@mail.com";
        final String incorrectEmail2 = "usermail.com";
        final String incorrectEmail3 = "user.1@com";
        final String incorrectEmail4 = "use r@mail.com.ua";
        final String incorrectEmail5 = "user@m ail.com";
        assertFalse(emailValidator.isValid(incorrectEmail1,constraintValidatorContext));
        assertFalse(emailValidator.isValid(incorrectEmail2,constraintValidatorContext));
        assertFalse(emailValidator.isValid(incorrectEmail3,constraintValidatorContext));
        assertFalse(emailValidator.isValid(incorrectEmail4,constraintValidatorContext));
        assertFalse(emailValidator.isValid(incorrectEmail5,constraintValidatorContext));
    }
}

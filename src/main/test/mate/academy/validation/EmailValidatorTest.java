package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    public void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    public void isValid_Ok() {
        String rightEmail = "alice.brown@gmail.com";
        assertTrue(emailValidator.isValid(rightEmail, constraintValidatorContext));
    }

    @Test
    public void isValid_NotOk() {
        String[] wrongEmails = {"bob.ua", "bob.boy@gmail.com.", "bob.boy@@gmail.com"};
        assertFalse(emailValidator.isValid(wrongEmails[0], constraintValidatorContext));
        assertFalse(emailValidator.isValid(wrongEmails[1], constraintValidatorContext));
        assertFalse(emailValidator.isValid(wrongEmails[2], constraintValidatorContext));
    }

    @Test
    public void isValidNull_NotOk() {
        assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}
package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    public void isValid_Ok() {
        assertTrue(emailValidator.isValid("user@gmail.com", constraintValidatorContext));
        assertTrue(emailValidator.isValid("User-123@mail.com", constraintValidatorContext));
        assertTrue(emailValidator.isValid("user_123@mail.com.ua", constraintValidatorContext));
    }

    @Test
    public void isValid_NotOk() {
        assertFalse(emailValidator.isValid("user", constraintValidatorContext));
        assertFalse(emailValidator.isValid("@mail.com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("user@com", constraintValidatorContext));
    }
}

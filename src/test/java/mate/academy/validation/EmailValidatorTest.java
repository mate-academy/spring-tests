package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        String email = "alisa21@gmail.com";
        assertNotNull(email);
        assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_invalidEmail_NotOk() {
        String email = "alisa@124@alisa...";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_emptyEmail_NotOk() {
        String email = "";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }
}

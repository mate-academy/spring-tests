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
    void isValid_Ok() {
        String email = "bob123@gmail.com";
        Assertions.assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValidInvalidEmail_NotOk() {
        String invalidEmail = "@bob@bob@";
        Assertions.assertFalse(emailValidator.isValid(invalidEmail, constraintValidatorContext));
        String emptyEmail = "";
        Assertions.assertFalse(emailValidator.isValid(emptyEmail, constraintValidatorContext));
    }
}

package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_validEmail_ok() {
        String email = "bob@i.ua";
        assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_validEmailWithSpecialSymbols_ok() {
        String email = "bob^%#*@i.ua";
        assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_emailWithoutDomainName_NotOk() {
        String email = "bob@";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_nullEmail_NotOk() {
        String email = null;
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_emptyEmail_NotOk() {
        String email = "";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_emailWithIncorrectDomainName_NotOk() {
        String email = "bob@gmail";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }
}

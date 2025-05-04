package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    String email;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        email = "bobby@sony.com";
    }

    @Test
    void isValid_Ok() {
        Assertions.assertFalse(emailValidator.isValid(null, null));
        Assertions.assertTrue(emailValidator.isValid(email, null));
    }

    @Test
    void isValid_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(null, null));
        Assertions.assertFalse(emailValidator.isValid("Not_email", null));
    }
}
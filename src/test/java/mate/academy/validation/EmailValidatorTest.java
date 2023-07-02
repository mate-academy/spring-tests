package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmailValidatorTest {
    private static EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void validEmail_ok() {
        String email = "test123@testmail.net";
        Assertions.assertTrue(emailValidator.isValid(email, null));
    }

    @Test
    void invalidEmail_notOk() {
        String email = "test123testmail.net";
        Assertions.assertFalse(emailValidator.isValid(email, null));
    }

    @Test
    void emailWithoutDot_notOk() {
        String email = "test123@testmailnet";
        Assertions.assertFalse(emailValidator.isValid(email, null));
    }

    @Test
    void emailWithoutAt_notOk() {
        String email = "test123testmail.net";
        Assertions.assertFalse(emailValidator.isValid(email, null));
    }

    @Test
    void emailWithTwoAt_notOk() {
        String email = "test123@testmail@net";
        Assertions.assertFalse(emailValidator.isValid(email, null));
    }

    @Test
    void emailWithTwoDots_notOk() {
        String email = "test123@testmail..net";
        Assertions.assertFalse(emailValidator.isValid(email, null));
    }
}

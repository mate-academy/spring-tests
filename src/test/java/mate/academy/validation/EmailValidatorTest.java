package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static EmailValidator emailValidator;
    private static final String incorrectEmail = "bcbvcxc";
    private static final String correctEmail = "bob@i.ua";

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid(correctEmail, null));
    }

    @Test
    void isValid_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(null, null));
        Assertions.assertFalse(emailValidator.isValid(incorrectEmail, null));
    }
}
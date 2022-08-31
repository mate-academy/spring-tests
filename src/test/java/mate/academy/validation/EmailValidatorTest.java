package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private EmailValidator emailValidator;

    @BeforeEach
    public void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    public void isValid_Ok() {
        String validEmail = "bob@i.ua";
        Assertions.assertTrue(emailValidator.isValid(validEmail, null));
    }

    @Test
    public void isValid_NotOk() {
        String invalidEmail = "bob$$i$$ua";
        Assertions.assertFalse(emailValidator.isValid(invalidEmail, null));
    }
}

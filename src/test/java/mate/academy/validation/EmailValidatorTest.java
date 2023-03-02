package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {
    private static EmailValidator emailValidator;

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_CorrectEmail_Ok() {
        String email = "User@gmail.com";
    }
}
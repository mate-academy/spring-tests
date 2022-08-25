package validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.validation.EmailValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EmailValidatorTest {
    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext validatorContext;

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
        validatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        String firstEmail = "admin@mail.com";
        String secondEmail = "bob@cinema.ua";
        String thirdEmail = "alice@media.ua";
        assertTrue(emailValidator.isValid(firstEmail, validatorContext));
        assertTrue(emailValidator.isValid(secondEmail, validatorContext));
        assertTrue(emailValidator.isValid(thirdEmail, validatorContext));
    }

    @Test
    void isValid_NotOk() {
        String firstEmail = "19d@@2@mail.com";
        String secondEmail = "bob+cinema.ua";
        String thirdEmail = "alice@media";
        assertFalse(emailValidator.isValid(firstEmail, validatorContext));
        assertFalse(emailValidator.isValid(secondEmail, validatorContext));
        assertFalse(emailValidator.isValid(thirdEmail, validatorContext));
    }

    @Test
    void isValid_nullCredentials_NotOk() {
        assertFalse(emailValidator.isValid(null, validatorContext));
    }
}

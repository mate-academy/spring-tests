package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "example@gmail.com";
    private static final String INVALID_EMAIL = "invalid";
    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext context;

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        assertTrue(emailValidator.isValid(VALID_EMAIL, context));
    }

    @Test
    void isValid_invalidEmail_NotOk() {
        assertFalse(emailValidator.isValid(INVALID_EMAIL, context));
    }

    @Test
    void isValid_null_NotOk() {
        assertFalse(emailValidator.isValid(null, context));
    }
}

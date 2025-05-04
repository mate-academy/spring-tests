package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    public static final String VALID_EMAIL = "bob@gmail.com";
    public static final String INVALID_EMAIL = "alice12345@";
    public static final String BLANK_EMAIL = "";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid(VALID_EMAIL, context));
    }

    @Test
    void notValid_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(INVALID_EMAIL, context));
    }

    @Test
    void nullEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(null, context));
    }

    @Test
    void blankEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(BLANK_EMAIL, context));
    }
}

package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "bob@@i.ua1";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_ok() {
        assertTrue(emailValidator.isValid(VALID_EMAIL, context));
    }

    @Test
    void isValid_notOk() {
        assertFalse(emailValidator.isValid(INVALID_EMAIL, context));
    }
}

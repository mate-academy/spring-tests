package mate.academy.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EmailValidatorTest {
    private ConstraintValidatorContext context;
    private EmailValidator emailValidator = new EmailValidator();

    @Before
    public void setUp() {
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    public void isValid_Ok() {
        assertTrue(emailValidator.isValid("bob123@gmail.com", context));
    }

    @Test
    public void isValid_NotOk() {
        assertFalse(emailValidator.isValid("bob", context));
    }

    @Test
    public void isValid_IsNull_NotOk() {
        assertFalse(emailValidator.isValid(null, context));
    }

    @Test
    public void isValid_emptyEmail_NotOk() {
        assertFalse(emailValidator.isValid("", context));
    }
}

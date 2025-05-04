package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String EMAIL = "cat@gmail.com";
    private static final String INVALID_EMAIL = "catgmail.com";

    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValidEmail_ok() {
        assertTrue(emailValidator.isValid(EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_WrongEmail_notOk() {
        assertFalse(emailValidator.isValid(INVALID_EMAIL, constraintValidatorContext));
    }
}

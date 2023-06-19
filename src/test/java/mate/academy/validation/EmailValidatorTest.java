package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String USER_EMAIL = "email@gmail.com";
    private static final String INCORRECT_USER_EMAIL = "incorrect.email";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValidTrue() {
        assertTrue(emailValidator.isValid(USER_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValidFalse() {
        assertFalse(emailValidator.isValid(INCORRECT_USER_EMAIL,
                constraintValidatorContext));
    }
}

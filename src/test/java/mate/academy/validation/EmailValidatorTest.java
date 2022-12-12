package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "user@domain.com";
    private static final String INVALID_EMAIL = "user_domain.com";
    private ConstraintValidatorContext constraintValidatorContext;
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_ValidEmail_True() {
        Assertions.assertTrue(emailValidator.isValid(VALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_InvalidEmail_True() {
        Assertions.assertFalse(emailValidator.isValid(INVALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_NullEmail_True() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}

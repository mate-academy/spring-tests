package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class EmailValidatorTest {
    private static ConstraintValidatorContext constraintValidatorContext;
    private static EmailValidator emailValidator;
    private static final String CORRECT_EMAIL = "email@gmail.com";
    private static final String INCORRECT_EMAIL = "email_gmail.com";

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_validEmail_ok() {
        assertTrue(emailValidator.isValid(CORRECT_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_invalidEmail_notOk() {
        assertFalse(emailValidator.isValid(INCORRECT_EMAIL, constraintValidatorContext));
    }
}

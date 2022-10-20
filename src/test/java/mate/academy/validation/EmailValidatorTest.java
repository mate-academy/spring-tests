package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "valid@email.com";
    private static final String INVALID_EMAIL = "Invalid";
    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    public void isValid_ValidEmail_Ok() {
        boolean valid = emailValidator.isValid(VALID_EMAIL, constraintValidatorContext);
        Assertions.assertTrue(valid, "Inputted email is not valid!");
    }

    @Test
    public void isValid_NotValidEmail_NotOk() {
        boolean valid = emailValidator.isValid(INVALID_EMAIL, constraintValidatorContext);
        Assertions.assertFalse(valid,"Inputted email valid! But must be invalid!" );
    }

    @Test
    public void isValid_NullEmail_NotOk() {
        boolean valid = emailValidator.isValid(null, constraintValidatorContext);
        Assertions.assertFalse(valid, "Inputted email valid! But must be invalid!");
    }
}

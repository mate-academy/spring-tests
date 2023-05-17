package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {

    private EmailValidator emailValidator = new EmailValidator();
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    public void is_Valid_Ok() {
        String email = "vasyl@gmail.com";
        Assertions.assertTrue(emailValidator.isValid(email,constraintValidatorContext));
    }

    @Test
    public void is_Valid_Not_Correct_Email() {
        String email = "not_valid_email";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    public void is_Valid_Null_Value_Email() {
        String email = null;
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

}

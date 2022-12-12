package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintValidatorContext;

class EmailValidatorTest {
    private static ConstraintValidatorContext constraintValidatorContext;
    private static EmailValidator emailValidator;

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_validEmail_Ok() {
        Assertions.assertTrue(emailValidator.isValid("user1@gmail.com",
                constraintValidatorContext));
    }

    @Test
    void isValid_invalidEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid("user1@gmail.@com",
                constraintValidatorContext));
    }

    @Test
    void isValid_nullEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}
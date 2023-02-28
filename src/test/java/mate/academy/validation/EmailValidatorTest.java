package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(emailValidator.isValid("bob@e.mail",
                constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        Assertions.assertFalse(emailValidator.isValid("bob@email",
                constraintValidatorContext));
    }

    @Test
    void isValid_null_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}

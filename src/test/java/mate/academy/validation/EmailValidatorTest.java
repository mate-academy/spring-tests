package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static ConstraintValidatorContext constraintValidatorContext;
    private static EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();

    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator
                .isValid("test@gmail.com",constraintValidatorContext));
    }

    @Test
    void isValid_Null_NotOk() {
        Assertions.assertFalse(emailValidator
                .isValid(null,constraintValidatorContext));
    }

    @Test
    void isValid_Text_NotOk() {
        Assertions.assertFalse(emailValidator
                .isValid("test.gmail.com",constraintValidatorContext));
    }
}

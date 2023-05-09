package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String EMAIL = "bob@i.ua";
    private ConstraintValidatorContext constraintValidatorContext;
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid(EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_emailIsNull_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    void isValid_emailIsWrong_NotOk() {
        Assertions.assertFalse(emailValidator.isValid("wrong@email", constraintValidatorContext));
    }
}

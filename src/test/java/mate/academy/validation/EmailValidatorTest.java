package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        String email = "bob@gmail.com";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_emailIsNull_notOk() {
        String email = null;
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_emailIsEmpty_notOk() {
        String email = "";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_emailWithoutAt_notOk() {
        String email = "bob-gmail.com";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_emailStartsWithAt_notOk() {
        String email = "@gmail.com";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}

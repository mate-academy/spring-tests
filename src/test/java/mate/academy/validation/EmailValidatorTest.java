package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class EmailValidatorTest {
    private ConstraintValidatorContext constraintValidatorContext;
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_valid_ok() {
        Assertions.assertTrue(emailValidator.isValid("Bob_dawn@gmail.com", constraintValidatorContext));
    }

    @Test
    void isValid_null_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    void isValid_doubleAddressSign_notOk() {
        Assertions.assertFalse(emailValidator.isValid("Bob@@i.ua", constraintValidatorContext));
    }
}
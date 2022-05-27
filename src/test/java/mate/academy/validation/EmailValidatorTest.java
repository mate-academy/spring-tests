package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_validEmail_Ok() {
        Assertions.assertTrue(emailValidator.isValid("bob@mail.com", constraintValidatorContext));
        Assertions.assertTrue(emailValidator.isValid("bob@i.ua", constraintValidatorContext));
        Assertions.assertTrue(emailValidator.isValid("bob_alice@mail.com",
                constraintValidatorContext));
        Assertions.assertTrue(emailValidator.isValid("bob555@mail.com",
                constraintValidatorContext));
        Assertions.assertTrue(emailValidator.isValid("BOB.ALICE@mail.com",
                constraintValidatorContext));
    }

    @Test
    void isValid_invalidEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid("bob@_@mail.com",
                constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("bob@mail.com@",
                constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("bob bob@mail.com",
                constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("bob bob@ma il.com",
                constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("bob bob@ma il.co m",
                constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("@mail.com", constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("bob@.com", constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("bob@mail.", constraintValidatorContext));
    }

    @Test
    void isValid_nullEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    void isValid_emptyEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid("", constraintValidatorContext));
    }
}

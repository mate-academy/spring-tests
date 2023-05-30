package mate.academy.validation;

import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValidTrue_Ok() {
        boolean emailTrue = emailValidator.isValid("hello@gmail.com", constraintValidatorContext);
        Assertions.assertTrue(emailTrue);
    }

    @Test
    void isValidFalse_NotOk() {
        boolean emailFalse = emailValidator.isValid("383838", constraintValidatorContext);
        Assertions.assertFalse(emailFalse);
    }

    @Test
    void isValid_Null_NotOk() {
        boolean emailFalse = emailValidator.isValid(null, constraintValidatorContext);
        Assertions.assertFalse(emailFalse);
    }
}

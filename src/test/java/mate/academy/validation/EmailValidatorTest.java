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
    public void isValid_ok() {
        Assertions.assertTrue(emailValidator.isValid("test@example.com",
                constraintValidatorContext));
    }

    @Test
    public void isValid_wrongFormat_notOk() {
        Assertions.assertFalse(emailValidator.isValid("invalidemail",
                constraintValidatorContext));
    }

    @Test
    public void isValid_nullEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}

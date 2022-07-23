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
    public void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    public void isValid_ok() {
        String email = "bob@i.ua";
        Assertions.assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    public void isValid_notValidEmail_ok() {
        String email = "Hello world";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    public void isValid_nullEmail_ok() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}

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
    public void isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid("test@email.com",
                constraintValidatorContext));
        Assertions.assertTrue(emailValidator.isValid("Bod28@gmail.com",
                constraintValidatorContext));
        Assertions.assertTrue(emailValidator.isValid("alice24@gmail.com",
                constraintValidatorContext));
    }

    @Test
    public void isValid_NotOk() {
        Assertions.assertFalse(emailValidator.isValid("bob", constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("@email.com", constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("bob@com", constraintValidatorContext));
    }
}

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
        Assertions.assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_WrongEmail() {
        Assertions.assertFalse(emailValidator.isValid("@gmail.com", constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("bob", constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid("bob@gmail", constraintValidatorContext));
    }
}

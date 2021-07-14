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
    void setUp(){
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_validEmail_ok(){
        String email = "lucy1234@gmail.com";
        Assertions.assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_invalidEmail_ok(){
        String email = "lkt___";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_nullEmail_ok(){
        String email = null;
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }
}
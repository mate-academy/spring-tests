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
    void email_isValid_True() {
        Assertions.assertTrue(emailValidator.isValid("bob@i.ua", constraintValidatorContext));
    }

    @Test
    void email_notValid_False() {
        Assertions.assertFalse(emailValidator.isValid("bob_i.ua", constraintValidatorContext));
    }

    @Test
    void email_isNull_False() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}

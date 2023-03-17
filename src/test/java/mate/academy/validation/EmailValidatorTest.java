package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator emailValidator = new EmailValidator();
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void validateEmail_Ok() {
        Assertions.assertTrue(emailValidator.isValid("bob@i.ua", constraintValidatorContext));
    }

    @Test
    void validateEmail_withoutAt_NotOk() {
        Assertions.assertFalse(emailValidator.isValid("bobi.ua", constraintValidatorContext));
    }

    @Test
    void validateEmail_withoutDomain_NotOK() {
        Assertions.assertFalse(emailValidator.isValid("bob@i.", constraintValidatorContext));
    }
}

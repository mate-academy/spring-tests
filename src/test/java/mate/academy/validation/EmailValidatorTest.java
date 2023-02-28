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
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        Assertions.assertEquals(true, emailValidator.isValid(
                "vvv@i.ua", constraintValidatorContext));
    }

    @Test
    void isValid_NotOk() {
        Assertions.assertNotEquals(true, emailValidator.isValid(
                "vvv@.ua", constraintValidatorContext));
    }
}

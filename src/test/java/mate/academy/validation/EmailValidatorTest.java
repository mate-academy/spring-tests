package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        boolean actual = emailValidator.isValid("bchupika@mate.academy",
                constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_notOk() {
        boolean actual = emailValidator.isValid("bob@gmail",
                constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_notOK() {
        boolean actual = emailValidator.isValid("bob",
                constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}
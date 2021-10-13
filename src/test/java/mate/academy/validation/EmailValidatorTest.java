package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EmailValidatorTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String NOT_EMAIL = "bobNeBob";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(emailValidator.isValid(EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        Assertions.assertFalse(emailValidator.isValid(NOT_EMAIL, constraintValidatorContext));
    }
}

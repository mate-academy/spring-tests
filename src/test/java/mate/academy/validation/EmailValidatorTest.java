package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bchupika@mate.academy";
    private static final String NOT_VALID_EMAIL = "user";
    private static final String BLANK_EMAIL = "";
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid(VALID_EMAIL,
                Mockito.mock(ConstraintValidatorContext.class)));

        Assertions.assertFalse(emailValidator.isValid(NOT_VALID_EMAIL,
                Mockito.mock(ConstraintValidatorContext.class)));
    }

    @Test
    void isValid_NotOk_False() {
        boolean actual = emailValidator.isValid(BLANK_EMAIL,
                Mockito.mock(ConstraintValidatorContext.class));
        Assertions.assertFalse(actual);
    }
}

package mate.academy.dao.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.validation.EmailValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@gmail.com";
    private static final String INVALID_EMAIL = "bob@";
    private static final String NULL_EMAIL = null;
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid(VALID_EMAIL,
                Mockito.mock(ConstraintValidatorContext.class)));
        Assertions.assertFalse(emailValidator.isValid(INVALID_EMAIL,
                Mockito.mock(ConstraintValidatorContext.class)));
    }

    @Test
    void isValid_notOk() {
        boolean actual = emailValidator.isValid(NULL_EMAIL,
                Mockito.mock(ConstraintValidatorContext.class));
        Assertions.assertFalse(actual);
    }
}

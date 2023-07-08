package mate.academy.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String TEST_EMAIL_OK = "artem@gmail.com";
    private ConstraintValidator<Email, String> emailValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        context = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        boolean actual = emailValidator.isValid(TEST_EMAIL_OK, context);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_Null_Email_Not_Ok() {
        boolean actual = emailValidator.isValid(null, context);
        Assertions.assertFalse(actual);
    }
}

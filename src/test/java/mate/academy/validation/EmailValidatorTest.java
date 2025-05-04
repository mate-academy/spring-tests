package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String INVALID_EMAIL = "bobgmail.com";

    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext context;

    @BeforeAll
    static void setup() {
        context = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_isSuccessful_ok() {
        Assertions.assertTrue(emailValidator.isValid(EMAIL, context));
    }

    @Test
    void isValid_invalidEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(INVALID_EMAIL, context));
    }
}

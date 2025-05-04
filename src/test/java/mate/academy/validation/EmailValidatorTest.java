package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void validateEmail_Ok() {
        String validEmail = "bob@i.ua";
        Assertions.assertTrue(emailValidator.isValid(validEmail, context));
    }

    @Test
    void validateEmail_invalidEmail_notOk() {
        String invalidEmail = "bob111-i.ua";
        Assertions.assertFalse(emailValidator.isValid(invalidEmail, context));
    }
}

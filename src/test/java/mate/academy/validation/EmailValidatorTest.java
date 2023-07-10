package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "bob@@i.ua1";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(emailValidator.isValid(VALID_EMAIL, context));
    }

    @Test
    void isValid_notOk() {
        Assertions.assertFalse(emailValidator.isValid(INVALID_EMAIL, context));
    }
}

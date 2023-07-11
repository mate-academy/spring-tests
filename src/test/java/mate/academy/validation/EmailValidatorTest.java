package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "bob@i.";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {

        Assertions.assertTrue(emailValidator.isValid(EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        Assertions.assertFalse(emailValidator.isValid(INVALID_EMAIL, constraintValidatorContext));
    }
}

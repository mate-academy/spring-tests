package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String NO_DOT_EMAIL = "b@4";
    private static final String NO_AT_EMAIL = "b4";
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
    void isValid_emailNull_notOk() {
        Assertions.assertFalse(emailValidator
                .isValid(null, constraintValidatorContext));
    }

    @Test
    void isValid_noDot_notOk() {
        Assertions.assertFalse(emailValidator
                .isValid(NO_DOT_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_noAt_notOk() {
        Assertions.assertFalse(emailValidator
                .isValid(NO_AT_EMAIL, constraintValidatorContext));
    }
}

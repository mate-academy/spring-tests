package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String EMAIL = "bob@i.ua";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_ok() {
        boolean actual = emailValidator.isValid(EMAIL, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_withoutSnail_notOk() {
        String email = "bobi.ua";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_withoutDot_notOk() {
        String email = "bob@iua";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_emptyFirstPart_notOk() {
        String email = "@i.ua";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_withoutTail_notOk() {
        String email = "bob@";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_Null_notOk() {
        boolean actual = emailValidator.isValid(null, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}

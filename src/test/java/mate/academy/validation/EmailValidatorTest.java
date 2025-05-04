package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "user@gmail.com";
    private static final String EMAIL_WITHOUT_AT_SIGN = "usergmail.com";
    private static final String EMAIL_WITHOUT_DOT = "user@gmailcom";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(emailValidator
                .isValid(VALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_emailIsNull_notOk() {
        Assertions.assertFalse(emailValidator
                .isValid(null, constraintValidatorContext));
    }

    @Test
    void idValid_emailWithoutAtSign_notOk() {
        Assertions.assertFalse(emailValidator
                .isValid(EMAIL_WITHOUT_AT_SIGN, constraintValidatorContext));
    }

    @Test
    void idValid_emailWithoutDot_notOk() {
        Assertions.assertFalse(emailValidator
                .isValid(EMAIL_WITHOUT_DOT, constraintValidatorContext));
    }
}

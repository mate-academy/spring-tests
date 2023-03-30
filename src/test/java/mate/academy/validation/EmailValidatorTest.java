package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String EMAIL_IS_WRONG = "alice";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(emailValidator.isValid(EMAIL,
                constraintValidatorContext));
    }

    @Test
    void isValid_emailIsWrong_notOk() {
        Assertions.assertFalse(emailValidator.isValid(EMAIL_IS_WRONG,
                constraintValidatorContext));
    }

    @Test
    void isValid_emailIsNull_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null,
                constraintValidatorContext));
    }
}

package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String INCORRECT_EMAIL = "alice";
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
                constraintValidatorContext),
                "Method should return true for email: " + EMAIL);
    }

    @Test
    void isValid_IncorrectEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(INCORRECT_EMAIL,
                constraintValidatorContext),
                "Method should return false for email: " + INCORRECT_EMAIL);
    }

    @Test
    void isValid_emailIsNull_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null,
                constraintValidatorContext),
                "Method should return false for email: " + null);
    }
}

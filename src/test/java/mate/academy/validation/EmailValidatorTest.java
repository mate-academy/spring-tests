package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String GOOD_EMAIL = "bob@gmail.com";
    private static final String BAD_EMAIL = "bob_yandex.sru";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_validEmail_Ok() {
        Assertions.assertTrue(emailValidator.isValid(GOOD_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_invalidEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(BAD_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_inputNull_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}

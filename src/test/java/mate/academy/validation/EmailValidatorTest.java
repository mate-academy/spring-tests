package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static ConstraintValidatorContext constraintValidatorContext;
    private static EmailValidator emailValidator;
    private static final String VALID_EMAIL = "user@i.ua";
    private static final String INVALID_EMAIL = "user@i@.ua";

    @BeforeAll
    static void beforeAll() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_validEmail_Ok() {
        Assertions.assertTrue(emailValidator.isValid(VALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_invalidEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(INVALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_emailNull_notOk() {
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}

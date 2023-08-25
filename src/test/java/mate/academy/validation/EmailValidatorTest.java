package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@mail.com";
    private static final String INVALID_EMAIL = "bob_mail.com";
    private static ConstraintValidatorContext constraintValidatorContext;
    private static EmailValidator emailValidator;

    @BeforeAll
    static void beforeAll() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid(VALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        Assertions.assertFalse(emailValidator.isValid(INVALID_EMAIL, constraintValidatorContext));
    }
}

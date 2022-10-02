package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String EMAIL = "bchupika@mate.academy";
    private static final String UNVALID_EMAIL = "mate.academy";
    private ConstraintValidatorContext constraintValidatorContext;
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator
                .isValid(EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        Assertions.assertFalse(emailValidator.isValid(UNVALID_EMAIL, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}

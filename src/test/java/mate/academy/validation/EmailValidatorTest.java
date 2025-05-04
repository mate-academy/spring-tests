package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_MAIL = "valid@example.com";
    private static final String INVALID_MAIL = "invalid_email";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContextImpl.class);
    }

    @Test
    void isValid_Ok() {
        boolean isValid = emailValidator.isValid(VALID_MAIL, constraintValidatorContext);
        Assertions.assertTrue(isValid);
    }

    @Test
    void isValid_invalidEmail_notOk() {
        boolean isValid = emailValidator.isValid(INVALID_MAIL, constraintValidatorContext);
        Assertions.assertFalse(isValid);
    }
}

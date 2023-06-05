package mate.academy.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@bob.ua";
    private static final String INVALID_EMAIL = "bob.bob.ua";
    private ConstraintValidator constraintValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContextImpl.class);
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(constraintValidator.isValid(VALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_InvalidEmail_NotOk() {
        Assertions.assertFalse(constraintValidator
                .isValid(INVALID_EMAIL, constraintValidatorContext));
    }
}

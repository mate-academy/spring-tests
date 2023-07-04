package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALIDATION_EMAIL = "bob@i.ua";
    private static final String NOT_VALIDATION_EMAIL = "alice.ua";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext validatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        validatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void emailValidator_OK() {
        Assertions.assertTrue(emailValidator.isValid(VALIDATION_EMAIL,validatorContext));
    }

    @Test
    void emailValidator_Not_Ok() {
        Assertions.assertFalse(emailValidator.isValid(NOT_VALIDATION_EMAIL,validatorContext));
    }

}

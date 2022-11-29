package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "denis@mail.ru";
    private static final String INVALID_EMAIL = "invalidEmail";
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_ok() {
        boolean validity = emailValidator.isValid(VALID_EMAIL, Mockito.mock(ConstraintValidatorContext.class));
        assertTrue(validity);
    }
    @Test
    void isValid_notOk() {
        boolean validity = emailValidator.isValid(INVALID_EMAIL, Mockito.mock(ConstraintValidatorContext.class));
        assertFalse(validity);
    }
}
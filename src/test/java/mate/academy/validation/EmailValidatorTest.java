package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "kk/i.ua";
    private EmailValidator emailValidator;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid(VALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_InvalidEmail_NotOk() {
        Assertions.assertTrue(emailValidator.isValid(INVALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_NullEmail_NotOk() {
        boolean valid = emailValidator.isValid(null, constraintValidatorContext);
        Assertions.assertFalse(valid);
    }
}

package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailValidatorTest {
    private EmailValidator emailValidator;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_validEmail_ok() {
        String email = "valid@gmail.com";
        assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_emailIsNull_ok() {
        String email = null;
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_missingAtSymbol_ok() {
        String email = "testgmail.com";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_missingDot_ok() {
        String email = "test@gmailcom";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_missingUsername_ok() {
        String email = "@gmail.com";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_missingDomainName_ok() {
        String email = "test@.com";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_missingDomain_ok() {
        String email = "test@gmail.";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_spaceSeparation_ok() {
        String email = "test @ gmail . com";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_reverseOrder_ok() {
        String email = "com.gmail@test";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_emptyEmail_ok() {
        String email = "";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }
}

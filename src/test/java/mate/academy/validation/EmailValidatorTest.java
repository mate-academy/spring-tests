package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmailValidatorTest {
    private EmailValidator emailValidator;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    @Order(1)
    void isValid_validEmail_ok() {
        String email = "valid@i.ua";
        boolean isValid = emailValidator.isValid(email, constraintValidatorContext);
        assertTrue(isValid);
    }

    @Test
    @Order(2)
    void isValid_noTextBeforeAt_notOk() {
        String email = "@i.ua";
        boolean isValid = emailValidator.isValid(email, constraintValidatorContext);
        assertFalse(isValid);
    }

    @Test
    @Order(3)
    void isValid_noTextAfterAt_notOk() {
        String email = "valid@";
        boolean isValid = emailValidator.isValid(email, constraintValidatorContext);
        assertFalse(isValid);
    }

    @Test
    @Order(4)
    void isValid_noAt_notOk() {
        String email = "valid_i.ua";
        boolean isValid = emailValidator.isValid(email, constraintValidatorContext);
        assertFalse(isValid);
    }

    @Test
    @Order(5)
    void isValid_noSymbolBeforeDot_notOk() {
        String email = "valid@.ua";
        boolean isValid = emailValidator.isValid(email, constraintValidatorContext);
        assertFalse(isValid);
    }

    @Test
    @Order(6)
    void isValid_noDot_notOk() {
        String email = "valid@i_ua";
        boolean isValid = emailValidator.isValid(email, constraintValidatorContext);
        assertFalse(isValid);
    }

    @Test
    @Order(7)
    void isValid_noSymbolAfterDot_notOk() {
        String email = "valid@i.";
        boolean isValid = emailValidator.isValid(email, constraintValidatorContext);
        assertFalse(isValid);
    }

    @Test
    @Order(8)
    void isValid_oneSymbolAfterDot_notOk() {
        String email = "valid@i.u";
        boolean isValid = emailValidator.isValid(email, constraintValidatorContext);
        assertFalse(isValid);
    }
}

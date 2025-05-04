package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
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
    void isValid_noValidEmail_notOk() {
        Set<String> invalidEmails = new HashSet<>();
        invalidEmails.add("@i.ua");
        invalidEmails.add("valid@");
        invalidEmails.add("valid_i.ua");
        invalidEmails.add("valid@.ua");
        invalidEmails.add("valid@i_ua");
        invalidEmails.add("valid@i.");
        invalidEmails.forEach(this::emailValidatorHelper);
    }

    @Test
    @Order(3)
    void isValid_nullEmail_notOk() {
        boolean isValid = emailValidator.isValid(null, constraintValidatorContext);
        assertFalse(isValid,
                "Method should return false for null email '%s'");
    }

    private void emailValidatorHelper(String email) {
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        assertFalse(actual,
                "Method should return false for email '%s'"
                        .formatted(email));
    }
}

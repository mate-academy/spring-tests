package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final List<String> VALID_EMAILS = List.of(
            "bob@i.ua", "alice@i.ua");
    private static final List<String> INVALID_EMAILS = List.of(
            "@i.ua", "bob", "bob@");
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_ok() {
        VALID_EMAILS.forEach(email -> assertTrue(
                emailValidator.isValid(email, constraintValidatorContext),
                String.format("Should be valid email: %s, but was invalid", email)));
    }

    @Test
    void isValid_notOk() {
        INVALID_EMAILS.forEach(email -> assertFalse(
                emailValidator.isValid(email, constraintValidatorContext),
                String.format("Should be invalid email: %s, but was valid", email)));
    }
}

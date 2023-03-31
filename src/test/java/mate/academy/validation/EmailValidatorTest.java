package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final List<String> VALID_EMAILS = List.of("dima@email.com",
            "vlad@email.com", "dasha@email.com");
    private static final List<String> WRONG_EMAILS = List.of("dima", "dasha@",
            "@gmail.com");
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void emailValid_ok() {
        VALID_EMAILS.forEach(email -> assertTrue(
                emailValidator.isValid(email, constraintValidatorContext),
                String.format("Email %s must be valid!", email)));
    }

    @Test
    void emailValid_notOk() {
        WRONG_EMAILS.forEach(email -> assertFalse(
                emailValidator.isValid(email, constraintValidatorContext),
                String.format("Email %s must be wrong!", email)));
    }
}

package mate.academy.validation;

import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailValidatorTest {
    private static EmailValidator emailValidator;
    @Mock
    private static ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    public void isValid_validEmail_ok() {
        Set<String> validEmails = new HashSet<>();
        validEmails.add("valid@email.com");
        validEmails.add("anotherValid@ema.il.uk");
        validEmails.add("1%&*q@77symbols.com");
        validEmails.add("TeSt$$789@123.MAIL.longdomain");
        validEmails.add("aaa*&%^$#@777mail.com.net.org");
        validEmails.forEach(email -> {
            boolean actual = emailValidator.isValid(email, context);
            Assertions.assertTrue(actual,
                    "Method should return true for email %s\n"
                            .formatted(email));
        });
    }

    @Test
    public void isValid_invalidEmail_ok() {
        Set<String> invalidEmails = new HashSet<>();
        invalidEmails.add("email");
        invalidEmails.add("email.com");
        invalidEmails.add("test.email.com");
        invalidEmails.add("test@email");
        invalidEmails.add("test@email.x");
        invalidEmails.add("test@email@another.one");
        invalidEmails.add("test@email.#$%");
        invalidEmails.add("test@%^&*.com");
        invalidEmails.forEach(email -> {
            boolean actual = emailValidator.isValid(email, context);
            Assertions.assertFalse(actual,
                    "Method should return false for email %s\n"
                            .formatted(email));
        });
    }

    @Test
    public void isValid_nullEmail_ok() {
        boolean actual = emailValidator.isValid(null, context);
        Assertions.assertFalse(actual,
                "Method should return false when email is null\n");
    }
}

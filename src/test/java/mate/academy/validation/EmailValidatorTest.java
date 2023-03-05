package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext contextMock;

    @BeforeEach
    void setUp() {
        contextMock = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        String validEmail = "user@gmail.com";
        boolean actual = emailValidator.isValid(validEmail, contextMock);
        assertTrue(actual);
    }

    @Test
    void isValid_EmailIsNull_Ok() {
        boolean actual = emailValidator.isValid(null, contextMock);
        assertFalse(actual);
    }

    @Test
    void isValid_InvalidEmail_Ok() {
        List<String> invalidEmails = new ArrayList<>();
        invalidEmails.add("usergmail.com");
        invalidEmails.add("usergmailcom");
        invalidEmails.add("usergmail@.co9");
        invalidEmails.add("12.com");
        invalidEmails.add("WWWWWWW.com");
        boolean actual = false;
        for (String email : invalidEmails) {
            if (emailValidator.isValid(email, contextMock)) {
                actual = true;
            }
        }
        assertFalse(actual);
    }
}

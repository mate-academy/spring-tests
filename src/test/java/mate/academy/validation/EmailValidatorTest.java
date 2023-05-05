package mate.academy.validation;

import java.util.List;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
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
    private List<String> invalidEmails = List.of();

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        String email = "bob@i.ua";
        Assertions.assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_NotValidEmail1_NotOk() {
        String email = "bob.ua";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_NotValidEmail2_NotOk() {
        String email = "bob@";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_NotValidEmail3_NotOk() {
        String email = "@ua";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }
}

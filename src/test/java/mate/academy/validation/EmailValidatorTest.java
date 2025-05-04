package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        String email = "bob@gmail.com";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_EmailIsNull_NotOk() {
        String email = null;
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_missingAtSign_NotOk() {
        String email = "bobgmail.com";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_dotInTheEnd_NotOk() {
        String email = "bob@gmail.com.";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_wrongDomain_NotOk() {
        String email = "bob@gmail";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_noDomain_NotOk() {
        String email = "bob@";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_noUsername_NotOk() {
        String email = "@gmail.com";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}

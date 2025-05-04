package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EmailValidatorTest {
    public static final String EMAIL = "modernboy349@gmail.com";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        boolean actual = emailValidator.isValid(EMAIL, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_noAtSign_NotOk() {
        String email = "modernboy349gmail.com";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_noDotCom_NotOk() {
        String email = "modernboy349@gmail";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_noDomain_NotOk() {
        String email = "modernboy349@com";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}

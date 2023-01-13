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
        String emailOk = "andrij@mail.com";
        boolean actualOk = emailValidator.isValid(emailOk, constraintValidatorContext);
        Assertions.assertTrue(actualOk);
    }

    @Test
    void isValid_wrongEmail_notOk() {
        String emailNotOk = "andrij@andrij@andrij.andrij";
        boolean actualNotOk = emailValidator.isValid(emailNotOk,constraintValidatorContext);
        Assertions.assertFalse(actualNotOk);

        String blankEmail = " ";
        boolean blankNotOk = emailValidator.isValid(blankEmail, constraintValidatorContext);
        Assertions.assertFalse(blankNotOk);
    }
}

package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_ok() {
        String email = "bob@i.ua";
        Assertions.assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void withSpecialSymbol_ok() {
        String email = "bob^%#*@i.ua";
        Assertions.assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void withoutDomainName_notOk() {
        String email = "bob@";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void nullName_notOk() {
        String email = null;
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void emptyName_notOk() {
        String email = "";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void notCorrectDomainName_notOk() {
        String email = "bob@gmail";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }
}

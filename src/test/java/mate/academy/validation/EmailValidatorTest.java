package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        String email = "vitaliy@mail.com";
        String email2 = "vitaliy@i.ua";
        Assertions.assertTrue(emailValidator.isValid(email, constraintValidatorContext));
        Assertions.assertTrue(emailValidator.isValid(email2, constraintValidatorContext));
    }

    @Test
    void isValid_withoutMail_notOk() {
        String email = "vitaliy@.com";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_withoutDomain_notOk() {
        String email = "vitaliy@mail";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_withoutAtSign_notOk() {
        String email = "vitaliy.mail.com";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }
}

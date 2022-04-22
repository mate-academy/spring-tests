package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator validator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        validator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_OK() {
        String email = "test@gmail.com";
        Assertions.assertTrue(validator.isValid(email, constraintValidatorContext));
        String emailShort = "i@i.com";
        Assertions.assertTrue(validator.isValid(emailShort, constraintValidatorContext));
        String emailLong = "testLongEmailLikeThis@gmailYahooSuperDomeain.com";
        Assertions.assertTrue(validator.isValid(emailLong, constraintValidatorContext));
    }

    @Test
    void isValid_NotOk() {
        String emailNoDomain = "test@.com";
        Assertions.assertFalse(validator.isValid(emailNoDomain, constraintValidatorContext));
        String emailNoName = "@gmail.com";
        Assertions.assertFalse(validator.isValid(emailNoName, constraintValidatorContext));
        String emailNoSobaka = "testgmail.com";
        Assertions.assertFalse(validator.isValid(emailNoSobaka, constraintValidatorContext));
    }
}

package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String USER_EMAIL = "bob@i.ua";
    private static final String USER_WRONG_EMAIL = "&ser%i,ua";
    private ConstraintValidatorContext constraintValidator;
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        constraintValidator = Mockito.spy(ConstraintValidatorContext.class);
        emailValidator = Mockito.spy(EmailValidator.class);
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid(USER_EMAIL, constraintValidator));
    }

    @Test
    void isValid_WrongEmail_NotOk() {
        Assertions.assertFalse(emailValidator.isValid(USER_WRONG_EMAIL, constraintValidator));
    }
}

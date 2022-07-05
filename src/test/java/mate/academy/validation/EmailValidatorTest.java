package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.util.UserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = Mockito.spy(EmailValidator.class);
    }

    @Test
    void isValid_Ok() {
        boolean actual = emailValidator.isValid(UserTestUtil.EMAIL,
                Mockito.mock(ConstraintValidatorContext.class));
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_NotOk() {
        boolean actual = emailValidator.isValid(UserTestUtil.INCORRECT_EMAIL,
                Mockito.mock(ConstraintValidatorContext.class));
        Assertions.assertFalse(actual);
    }
}
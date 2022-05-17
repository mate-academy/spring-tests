package mate.academy.validation;

import mate.academy.util.UserUtilForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserUtilForTest userUtil;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        userUtil = new UserUtilForTest();
    }

    @Test
    void isValid_validEmail_ok() {
        boolean actual = emailValidator.isValid(userUtil.getBorisEmail(),
                constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_notValidEmail_notOk() {
        boolean actual = emailValidator.isValid(userUtil.getBorisEmail().substring(0,6),
                constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

}

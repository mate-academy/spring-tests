package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String USER_NAME = "bob@i.ua";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(emailValidator.isValid(USER_NAME, constraintValidatorContext));
    }

    @Test
    void isValid_incorrectEmail_notOk() {
        Assertions.assertTrue(emailValidator.isValid("bob123@i.ua", constraintValidatorContext));
    }
}

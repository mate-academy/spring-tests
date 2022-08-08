package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_validData_ok() {
        Assertions.assertTrue(emailValidator.isValid(VALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_emailWithoutEt_notOk() {
        Assertions.assertFalse(emailValidator.isValid("bobi.ua", constraintValidatorContext));
    }

    @Test
    void isValid_emailWithoutDomain_notOk() {
        Assertions.assertFalse(emailValidator.isValid("bob@i", constraintValidatorContext));
    }
}

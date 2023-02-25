package mate.academy.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailValidatorTest {
    private static ConstraintValidatorContext constraintValidatorContext;
    private static EmailValidator emailValidator;
    private static final String CORRECT_EMAIL = "bchupika@mate.academy";
    private static final String INCORRECT_EMAIL = "bchupika&mate.academy";

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_validData_ok() {
        assertTrue(emailValidator.isValid(CORRECT_EMAIL,constraintValidatorContext));
    }
    @Test
    void isValid_inValidData_notOk() {
        assertFalse(emailValidator.isValid(INCORRECT_EMAIL,constraintValidatorContext));
    }
}
package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static ConstraintValidator<Email, String> constraintValidator;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void beforeAll() {
        constraintValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class); {
        }
    }

    @Test
    void isValid_nullEmail_notOK() {
        assertFalse(constraintValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    void isValid_notValidEmail_notOK() {
        assertFalse(constraintValidator.isValid("null", constraintValidatorContext));
        assertFalse(constraintValidator.isValid("@i.ua", constraintValidatorContext));
        assertFalse(constraintValidator.isValid("bob.i.ua", constraintValidatorContext));
        assertFalse(constraintValidator.isValid("bob@@i.ua", constraintValidatorContext));
        assertFalse(constraintValidator.isValid("bob@i@.ua", constraintValidatorContext));
        assertFalse(constraintValidator.isValid("bob@iua.", constraintValidatorContext));
        assertFalse(constraintValidator.isValid("bob@.ua", constraintValidatorContext));
        assertFalse(constraintValidator.isValid("bob@i..ua", constraintValidatorContext));
        assertFalse(constraintValidator.isValid("bob@i.ua.", constraintValidatorContext));
        assertFalse(constraintValidator.isValid("bob.@i.ua.", constraintValidatorContext));
        assertFalse(constraintValidator.isValid("bob,@i.ua", constraintValidatorContext));
    }

    @Test
    void isValid_validEmail_OK() {
        assertTrue(constraintValidator.isValid("bob@i.ua", constraintValidatorContext));
        assertTrue(constraintValidator.isValid("b.ob@i.ua", constraintValidatorContext));
        assertTrue(constraintValidator.isValid("bob@i.ua.ua", constraintValidatorContext));
        assertTrue(constraintValidator.isValid("b!ob@i.ua", constraintValidatorContext));
        assertTrue(constraintValidator.isValid("bob.bob@i.ua", constraintValidatorContext));
        assertTrue(constraintValidator.isValid("Bob@gmail.com", constraintValidatorContext));
        assertTrue(constraintValidator.isValid("bob.@i.ua", constraintValidatorContext));
        assertTrue(constraintValidator.isValid("bobbobobobob@i.ua", constraintValidatorContext));
        assertTrue(constraintValidator.isValid("bob@i.ua.ua.com", constraintValidatorContext));
        assertTrue(constraintValidator.isValid("bob_BOB@i.ua", constraintValidatorContext));
        assertTrue(constraintValidator.isValid("bob.bob_bob/BOB@i.ua", constraintValidatorContext));
    }
}

package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContextImpl.class);
    }

    @Test
    void isValidOk() {
        Assertions.assertTrue(emailValidator.isValid("alise@i.ua", constraintValidatorContext));
    }

    @Test
    void isValid_True() {
        assertTrue(emailValidator.isValid("alise@i.ua", null));
        assertTrue(emailValidator.isValid("alice@g.co", null));
        assertTrue(emailValidator.isValid("alice@mate.academy", null));
    }

    @Test
    void isValid_False() {
        assertFalse(emailValidator.isValid("alice@.ua", null));
        assertFalse(emailValidator.isValid("alice@g.", null));
        assertFalse(emailValidator.isValid("alice", null));
        assertFalse(emailValidator.isValid("alice#i.ua", null));
        assertFalse(emailValidator.isValid(null, null));
    }
}
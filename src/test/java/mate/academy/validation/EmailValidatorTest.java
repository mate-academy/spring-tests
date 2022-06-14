package mate.academy.validation;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {
    private static final String VALID_VALUE_1 = "bob@i.ua";
    private static final String VALID_VALUE_2 = "bo.b@i.ua";
    private static final String VALID_VALUE_3 = "b_o.b@i.u.uuaa";
    private static final String INVALID_VALUE_1 = "@bobi.ua";
    private static final String INVALID_VALUE_2 = "bobi.ua";
    private static final String INVALID_VALUE_3 = "bob@iua";
    private static final String INVALID_VALUE_4 = "bo@b@i.ua";
    private static final String INVALID_VALUE_5 = "bob@i..ua";
    private static final String INVALID_VALUE_6 = " bob@i.ua";
    private static final String INVALID_VALUE_7 = "b ob@i.ua";
    private static final String INVALID_VALUE_8 = "bobi@i.u_ua";
    private final EmailValidator emailValidator = new EmailValidator();
    private final ConstraintValidatorContext constraintValidatorContext =
            Mockito.mock(ConstraintValidatorContext.class);

    @Test
    void isValid_Ok() {
        assertTrue(emailValidator.isValid(VALID_VALUE_1, constraintValidatorContext));
        assertTrue(emailValidator.isValid(VALID_VALUE_2, constraintValidatorContext));
        assertTrue(emailValidator.isValid(VALID_VALUE_3, constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        assertFalse(emailValidator.isValid(INVALID_VALUE_1, constraintValidatorContext));
        assertFalse(emailValidator.isValid(INVALID_VALUE_2, constraintValidatorContext));
        assertFalse(emailValidator.isValid(INVALID_VALUE_3, constraintValidatorContext));
        assertFalse(emailValidator.isValid(INVALID_VALUE_4, constraintValidatorContext));
        assertFalse(emailValidator.isValid(INVALID_VALUE_5, constraintValidatorContext));
        assertFalse(emailValidator.isValid(INVALID_VALUE_6, constraintValidatorContext));
        assertFalse(emailValidator.isValid(INVALID_VALUE_7, constraintValidatorContext));
        assertFalse(emailValidator.isValid(INVALID_VALUE_8, constraintValidatorContext));
    }

    @Test
    void isValid_null_notOk() {
        assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}
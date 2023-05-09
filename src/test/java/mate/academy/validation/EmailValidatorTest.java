package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL_1 = "john@me.com";
    private static final String VALID_EMAIL_2 = "JOHN@ME.COM";
    private static final String VALID_EMAIL_3 = "john123@me.com";
    private static final String VALID_EMAIL_4 = "john_smith@me.com";
    private static final String VALID_EMAIL_5 = "john.smith@me.com";
    private static final String INVALID_EMAIL_1 = "@me.com";
    private static final String INVALID_EMAIL_2 = "@john@me.com";
    private static final String INVALID_EMAIL_3 = "john smith@me.com";
    private static final String INVALID_EMAIL_4 = "john.smith@ me.com";
    private static final String INVALID_EMAIL_5 = "john.smith@me.";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_validEmail_Ok() {
        Assertions.assertTrue(emailValidator.isValid(
                VALID_EMAIL_1, constraintValidatorContext));
        Assertions.assertTrue(emailValidator.isValid(
                VALID_EMAIL_2, constraintValidatorContext));
        Assertions.assertTrue(emailValidator.isValid(
                VALID_EMAIL_3, constraintValidatorContext));
        Assertions.assertTrue(emailValidator.isValid(
                VALID_EMAIL_4, constraintValidatorContext));
        Assertions.assertTrue(emailValidator.isValid(
                VALID_EMAIL_5, constraintValidatorContext));
    }

    @Test
    void isValid_validEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(
                INVALID_EMAIL_1, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(
                INVALID_EMAIL_2, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(
                INVALID_EMAIL_3, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(
                INVALID_EMAIL_4, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(
                INVALID_EMAIL_5, constraintValidatorContext));
    }

    @Test
    void isValid_nullEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(
                null, constraintValidatorContext));
    }

    @Test
    void isValid_emptyEmail_notOk() {
        Assertions.assertFalse(emailValidator.isValid(
                "", constraintValidatorContext));
    }
}

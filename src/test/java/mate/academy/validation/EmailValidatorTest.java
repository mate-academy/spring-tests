package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String FIRST_VALID_EMAIL = "iskamele@gmail.com";
    private static final String SECOND_VALID_EMAIL = "bob@i.ua";
    private static final String THIRD_VALID_EMAIL = "max@mate-academy.com";
    private static final String FIRST_NOT_VALID_EMAIL = "iskamelegmail.com";
    private static final String SECOND_NOT_VALID_EMAIL = "bob@i";
    private static final String THIRD_NOT_VALID_EMAIL = "bob@";
    private static final String EMPTY_EMAIL = "";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_validEmails_Ok() {
        //act & assert
        Assertions.assertTrue(emailValidator
                .isValid(FIRST_VALID_EMAIL, constraintValidatorContext));
        Assertions.assertTrue(emailValidator
                .isValid(SECOND_VALID_EMAIL, constraintValidatorContext));
        Assertions.assertTrue(emailValidator
                .isValid(THIRD_VALID_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_notValidEmails_NotOk() {
        //act & assert
        Assertions.assertFalse(emailValidator
                .isValid(FIRST_NOT_VALID_EMAIL, constraintValidatorContext));
        Assertions.assertFalse(emailValidator
                .isValid(SECOND_NOT_VALID_EMAIL, constraintValidatorContext));
        Assertions.assertFalse(emailValidator
                .isValid(THIRD_NOT_VALID_EMAIL, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(EMPTY_EMAIL, constraintValidatorContext));
    }

    @Test
    void isValid_null_NotOk() {
        //act & assert
        Assertions.assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}

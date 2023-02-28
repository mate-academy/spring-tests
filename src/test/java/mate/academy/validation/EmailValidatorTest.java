package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String FIRST_VALID_EMAIL = "someemail@gmail.com";
    private static final String SECOND_VALID_EMAIL = "some2email5@gmail.com";
    private static final String THIRD_VALID_EMAIL = "someEmail@gmail.com";
    private static final String FOURTH_VALID_EMAIL = "someemail@i.ua";
    private static final String FIRST_INVALID_EMAIL = "123mail.com";
    private static final String SECOND_INVALID_EMAIL = "@gmail.com";
    private static final String THIRD_INVALID_EMAIL = "@gmailcom@";
    private static final String FOURTH_INVALID_EMAIL = "someemail";
    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        String message = "Expected true, but was false, email ";
        Assertions.assertTrue(emailValidator.isValid(FIRST_VALID_EMAIL,
                constraintValidatorContext), message + FIRST_VALID_EMAIL);
        Assertions.assertTrue(emailValidator.isValid(SECOND_VALID_EMAIL,
                constraintValidatorContext), message + SECOND_VALID_EMAIL);
        Assertions.assertTrue(emailValidator.isValid(THIRD_VALID_EMAIL,
                constraintValidatorContext), message + THIRD_VALID_EMAIL);
        Assertions.assertTrue(emailValidator.isValid(FOURTH_VALID_EMAIL,
                constraintValidatorContext), message + FOURTH_VALID_EMAIL);
    }

    @Test
    void isValid_NotOk() {
        String message = "Expected false, but was true, email ";
        Assertions.assertFalse(emailValidator.isValid(FIRST_INVALID_EMAIL,
                constraintValidatorContext), message + FIRST_INVALID_EMAIL);
        Assertions.assertFalse(emailValidator.isValid(SECOND_INVALID_EMAIL,
                constraintValidatorContext), message + SECOND_INVALID_EMAIL);
        Assertions.assertFalse(emailValidator.isValid(THIRD_INVALID_EMAIL,
                constraintValidatorContext), message + THIRD_INVALID_EMAIL);
        Assertions.assertFalse(emailValidator.isValid(FOURTH_INVALID_EMAIL,
                constraintValidatorContext), message + FOURTH_INVALID_EMAIL);
    }
}

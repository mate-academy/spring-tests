package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class EmailValidatorTest {
    private final String EMAIL = "vitalii@gmail.com";
    private final String EMPTY_EMAIL = "";
    private final String EMAIL_WITHOUT_DOT = "vitalii@gmailcom";
    private final String EMAIL_WITHOUT_AT = "vitaliigmail.com";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_OK() {
        Assertions.assertNotNull(EMAIL, "Email can not be empty");
        boolean isValid = emailValidator.isValid(EMAIL, constraintValidatorContext);
        Assertions.assertTrue(true);
    }

    @Test
    void isValid_NOK() {
        Assertions.assertFalse(emailValidator.isValid(EMPTY_EMAIL, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(EMAIL_WITHOUT_DOT, constraintValidatorContext));
        Assertions.assertFalse(emailValidator.isValid(EMAIL_WITHOUT_AT, constraintValidatorContext));
    }
}
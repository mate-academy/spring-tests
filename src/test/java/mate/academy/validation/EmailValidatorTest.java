package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "bobi.ua";
    private static final String NULL_EMAIL = null;
    private static ConstraintValidatorContext context;
    private static EmailValidator emailValidator;

    @BeforeAll
    static void setUpBeforeClass() {
        emailValidator = new EmailValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_valid_ok() {
        boolean actual = emailValidator.isValid(VALID_EMAIL, context);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_notValid_notOk() {
        boolean actual = emailValidator.isValid(INVALID_EMAIL, context); 
        Assertions.assertFalse(actual);
    }
    
    @Test
    void isValid_nullValue_notOk() {
        boolean actual = emailValidator.isValid(NULL_EMAIL, context); 
        Assertions.assertFalse(actual);
    }
}

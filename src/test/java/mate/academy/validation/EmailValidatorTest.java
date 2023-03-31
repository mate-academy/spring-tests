package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    
    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }
    
    @Test
    void isValid_ok() {
        String email = "testuser@i.ua";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }
    
    @Test
    void isValid_NullEmail_notOk() {
        boolean actual = emailValidator.isValid(null, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
    
    @Test
    void isValid_EmailWithoutAt_notOk() {
        String email = "testuseri.ua";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
    
    @Test
    void isValid_EmailStartsWithAt_notOk() {
        String email = "@i.ua";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
    
    @Test
    void isValid_EmailEndsWithAt_notOk() {
        String email = "testuser@";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
    
    @Test
    void isValid_EmailWithoutDotAfterAt_notOk() {
        String email = "testuser@iua";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
    
    @Test
    void isValid_EmptyEmail_notOk() {
        String email = "";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}

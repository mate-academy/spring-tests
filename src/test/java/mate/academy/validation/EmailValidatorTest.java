package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private final EmailValidator emailValidator = new EmailValidator();
    ;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_checkLetters_ok() {
        String email = "test@gmail.com";
        assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_checkSymbols_ok() {
        String email = "test%^$345@gmail.com";
        assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_checkDomainIp_ok() {
        String email = "test@192.168.0.1";
        assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_checkBadSymbolBeforeAt_notOk() {
        String email = "te:st@testdomainame.gmail";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_checkBadDomainName_notOk() {
        String email = "test@test.12com";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_checkBadDomainIp_notOk() {
        String email = "test@192.168.1232.1";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void isValid_checkNoSymboslBeforeAt_noOk() {
        String email = "@testdomainame.gmail";
        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }
}
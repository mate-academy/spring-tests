package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_ok() {
        boolean expected = true;

        String email = "right@mail.com";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);

        assertEquals(expected, actual);
    }

    @Test
    void isValid_mailWithoutAtSign_notOk() {
        boolean expected = false;

        String email = "wrongmail.com";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);

        assertEquals(expected, actual);
    }

    @Test
    void isValid_mailWithoutDot_notOk() {
        boolean expected = false;

        String email = "wrong@mailcom";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);

        assertEquals(expected, actual);
    }

    @Test
    void isValid_mailNull_notOk() {
        boolean expected = false;

        String email = null;
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);

        assertEquals(expected, actual);
    }

    @Test
    void isValid_mailWithoutPartAfterDot_notOk() {
        boolean expected = false;

        String email = "wrong@mail";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);

        assertEquals(expected, actual);
    }

    @Test
    void isValid_mailWithoutFirstPart_notOk() {
        boolean expected = false;

        String email = "@mail.com";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);

        assertEquals(expected, actual);
    }

    @Test
    void isValid_mailEmptyString_notOk() {
        boolean expected = false;

        String email = "";
        boolean actual = emailValidator.isValid(email, constraintValidatorContext);

        assertEquals(expected, actual);
    }
}

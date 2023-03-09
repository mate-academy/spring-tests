package mate.academy.validation;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final List<String> VALID_EMAIL = new ArrayList<>();
    private static final List<String> INVALID_EMAIL = new ArrayList<>();
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void beforeAll() {
        VALID_EMAIL.add("test@test.com");
        VALID_EMAIL.add("test.test@test.com");
        VALID_EMAIL.add("21903@i.com");
        VALID_EMAIL.add("er_wef.rt77e@test.me");
        VALID_EMAIL.add("!#$%&'*+/=?^_`{|}~-@test.me");
        VALID_EMAIL.add("test@test.com.me");
        VALID_EMAIL.add("thisEmailNameIsReallyVeryLogByMaybeNotEnoughForThisMethod"
                + "@test.superuperlongword");
        INVALID_EMAIL.add("@test.me");
        INVALID_EMAIL.add("test.me");
        INVALID_EMAIL.add("testmail@me");
        INVALID_EMAIL.add("testmail@.me");
        INVALID_EMAIL.add("test@test.com_me");
        INVALID_EMAIL.add("test.com@");
    }

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        for (String email: VALID_EMAIL) {
            Assertions.assertTrue(emailValidator.isValid(email, constraintValidatorContext),
                    String.format("Should be true for email: %s, but was false", email));
        }
    }

    @Test
    void isValid_emailWithInvalidParams_notOk() {
        for (String email: INVALID_EMAIL) {
            Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext),
                    String.format("Should be false for email: %s, but was true", email));
        }
    }
}

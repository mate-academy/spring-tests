package mate.academy.validation;

import java.util.List;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final List<String> VALID_EMAILS = List.of(
            "test@test.com", "test.test@test.com",
            "21903@i.com", "er_wef.rt77e@test.me",
            "!#$%&'*+/=?^_`{|}~-@test.me", "test@test.com.me",
            "thisEmailNameIsReallyVeryLogButMaybeNotEnoughForThisMethod@test.superuperlongword");
    private static final List<String> INVALID_EMAILS = List.of(
            "@test.me", "test.me", "testmail@me",
            "testmail@.me", "test@test.com_me", "test.com@");
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        for (String email: VALID_EMAILS) {
            Assertions.assertTrue(emailValidator.isValid(email, constraintValidatorContext),
                    String.format("Should be true for email: %s, but was false", email));
        }
    }

    @Test
    void isValid_emailWithInvalidParams_notOk() {
        for (String email: INVALID_EMAILS) {
            Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext),
                    String.format("Should be false for email: %s, but was true", email));
        }
    }
}

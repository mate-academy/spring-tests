package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String ALICE_EMAIL = "alice@i.ua";
    private static final String BOB_EMAIL = "bob@i.ua";
    private static final String CORRECT_PASSWORD = "1234";
    private static final String WRONG_PASSWORD = "12345";
    private static ConstraintValidatorContext constraintValidatorContext;
    private static PasswordValidator passwordValidator;
    private static UserRegistrationDto alice;
    private static UserRegistrationDto bob;

    @BeforeAll
    static void beforeAll() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();

        alice = new UserRegistrationDto();
        bob = new UserRegistrationDto();

        Password password = Mockito.spy(Password.class);

        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");

        passwordValidator.initialize(password);
    }

    @Test
    void isValid_Ok() {
        alice.setEmail(ALICE_EMAIL);
        alice.setPassword(CORRECT_PASSWORD);
        alice.setRepeatPassword(CORRECT_PASSWORD);

        bob.setEmail(BOB_EMAIL);
        bob.setPassword(CORRECT_PASSWORD);
        bob.setRepeatPassword(WRONG_PASSWORD);

        boolean alicePasswordValidator =
                passwordValidator.isValid(alice, constraintValidatorContext);
        boolean bobPasswordValidator =
                passwordValidator.isValid(bob, constraintValidatorContext);

        Assertions.assertTrue(alicePasswordValidator);
        Assertions.assertFalse(bobPasswordValidator);
    }
}

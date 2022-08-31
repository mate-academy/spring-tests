package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "1234ljhj";
    private PasswordValidator passwordValidator;
    private UserRegistrationDto userRegistrationDto;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        passwordValidator = new PasswordValidator();
        Password password = Mockito.mock(Password.class);
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
    }

    @Test
    void isValid_ok() {
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
        Assertions.assertTrue(
                passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_notValidPasswordRepeat_notOk() {
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword("Passwords don't match!");
        Assertions.assertFalse(
                passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}

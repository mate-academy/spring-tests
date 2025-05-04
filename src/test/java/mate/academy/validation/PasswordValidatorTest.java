package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "bob12345";
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        userRegistrationDto = new UserRegistrationDto();
        Password password = Mockito.mock(Password.class);
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
    }

    @Test
    void isValid_validData_ok() {
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
        Assertions.assertTrue(
                passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_notValidPasswordRepeat_notOk() {
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword("Not valid repeatPassword");
        Assertions.assertFalse(
                passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}

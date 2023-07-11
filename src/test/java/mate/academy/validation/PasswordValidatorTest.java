package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String PASSWORD = "12345678";
    private static final String INVALID_PASSWORD = "123456789";
    private UserRegistrationDto userRegistrationDto;
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(INVALID_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }
}

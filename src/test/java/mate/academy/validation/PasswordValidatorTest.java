package mate.academy.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private ConstraintValidator<Password, UserRegistrationDto> passwordValidator;
    private Password password;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        password = Mockito.mock(Password.class);
        passwordValidator = new PasswordValidator();
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
        userRegistrationDto = new UserRegistrationDto();
    }

    @Test
    void isValid_samePasswords_ok() {
        userRegistrationDto.setPassword("NicePassword");
        userRegistrationDto.setRepeatPassword("NicePassword");
        Assertions.assertTrue(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_differentPasswords_notOk() {
        userRegistrationDto.setPassword("NicePassword");
        userRegistrationDto.setRepeatPassword("BetterPassword");
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }
}

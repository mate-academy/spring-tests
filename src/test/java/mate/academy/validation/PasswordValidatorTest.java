package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.exception.validation.Password;
import mate.academy.exception.validation.PasswordValidator;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {

    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);

    }

    @Test
    public void isValid_ok() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setPassword("password");
        dto.setRepeatPassword("password");
        Assertions.assertTrue(passwordValidator.isValid(dto, constraintValidatorContext));
    }

    @Test
    public void isValid_notMatchPassword_notOk() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setPassword("password");
        dto.setRepeatPassword("anotherPassword");
        Assertions.assertFalse(passwordValidator.isValid(dto, constraintValidatorContext));
    }
}

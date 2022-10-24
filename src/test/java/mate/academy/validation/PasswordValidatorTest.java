package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        registrationDto = new UserRegistrationDto();
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
    }

    @Test
    void isValid_Ok() {
        registrationDto.setEmail("vvv@i.ia");
        registrationDto.setPassword("12341234");
        registrationDto.setRepeatPassword("12341234");
        Assertions.assertTrue(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_NotValidPassword_NotOk() {
        registrationDto.setEmail("vvv@i.ia");
        registrationDto.setPassword("12341234");
        registrationDto.setRepeatPassword("1234123");
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));
    }
}

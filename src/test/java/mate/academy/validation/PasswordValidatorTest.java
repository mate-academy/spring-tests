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
    private Password password;

    @BeforeEach
    void setUp(){
        password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(password);
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_matchingPassword_ok(){
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("lucy1234@gmail.com");
        registrationDto.setPassword("12345tyr");
        registrationDto.setRepeatPassword("12345tyr");
        Assertions.assertTrue(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_notMatchingPassword_ok(){
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("lucy1234@gmail.com");
        registrationDto.setPassword("kjhgf");
        registrationDto.setRepeatPassword("12345tyr");
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_nullPassword_ok(){
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("lucy1234@gmail.com");
        registrationDto.setPassword(null);
        registrationDto.setRepeatPassword("12345tyr");
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }

}
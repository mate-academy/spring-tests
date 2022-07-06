package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class PasswordValidatorTest {
    private static final String PASSWORD = "12345678";
    private static final String INVALID_PASSWORD = "12345679";
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValid() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("bob@i.ua");
        registrationDto.setPassword("1234");
        registrationDto.setRepeatPassword("1234");
        Assertions.assertTrue(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();

        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword(INVALID_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));

        registrationDto.setPassword(INVALID_PASSWORD);
        registrationDto.setRepeatPassword(PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));

        registrationDto.setPassword(null);
        registrationDto.setRepeatPassword(PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));

        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));
    }
}
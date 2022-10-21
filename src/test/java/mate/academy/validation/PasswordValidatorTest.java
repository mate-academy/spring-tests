package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private Password constraintAnnotation;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintAnnotation = Mockito.mock(Password.class);
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_passwordsMatch_OK() {
        String checkedPassword = "x#89_uierRR";
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("bob@i.ua");
        userRegistrationDto.setPassword(checkedPassword);
        userRegistrationDto.setRepeatPassword(checkedPassword);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
        boolean actual = passwordValidator.isValid(userRegistrationDto,constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_passwordsNotMatch_False() {
        String checkedPassword = "x#89_uierRR";
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("bob@i.ua");
        userRegistrationDto.setPassword(checkedPassword);
        userRegistrationDto.setRepeatPassword("different");
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
        boolean actual = passwordValidator.isValid(userRegistrationDto,constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_passwordsNull_False() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("bob@i.ua");
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(null);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
        boolean actual = passwordValidator.isValid(userRegistrationDto,constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}

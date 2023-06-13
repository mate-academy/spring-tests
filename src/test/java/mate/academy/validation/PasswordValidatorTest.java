package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String FIRST_PASSWORD = "password";
    private static final String SECOND_PASSWORD = "different";
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        userRegistrationDto = new UserRegistrationDto();
        Password annotation = Mockito.mock(Password.class);
        Mockito.when(annotation.field()).thenReturn("password");
        Mockito.when(annotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(annotation);
    }

    @Test
    void isValid_similarPasswords_Ok() {
        //arrange
        userRegistrationDto.setPassword(FIRST_PASSWORD);
        userRegistrationDto.setRepeatPassword(FIRST_PASSWORD);

        //act & assert
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_differentPasswords_NotOk() {
        //arrange
        userRegistrationDto.setPassword(FIRST_PASSWORD);
        userRegistrationDto.setRepeatPassword(SECOND_PASSWORD);

        //act & assert
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_nullPasswordField_NotOk() {
        userRegistrationDto.setRepeatPassword(SECOND_PASSWORD);

        //act & assert
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_nullPasswords_NotOk() {
        //act & assert
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }
}

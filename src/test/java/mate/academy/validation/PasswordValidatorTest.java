package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;


class PasswordValidatorTest {
    private static final String PASSWORD_FIELD_NAME = "password";
    private static final String REPEAT_PASSWORD_FIELD_NAME = "repeatPassword";
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;
    private static Password passwordConstraint;
    private static UserRegistrationDto userRegistrationDto;

    @BeforeAll
    static void beforeAll() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordConstraint = Mockito.mock(Password.class);
        userRegistrationDto = new UserRegistrationDto();
    }

    @Test
    public void isValid_EqualedPassword_Ok() {
        userRegistrationDto.setEmail("user@email.com");
        userRegistrationDto.setPassword("user1234");
        userRegistrationDto.setRepeatPassword("user1234");
        Mockito.when(passwordConstraint.field()).thenReturn(PASSWORD_FIELD_NAME);
        Mockito.when(passwordConstraint.fieldMatch()).thenReturn(REPEAT_PASSWORD_FIELD_NAME);
        passwordValidator.initialize(passwordConstraint);
        boolean valid = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertTrue(valid, "Inputted passwords do not match!");
    }

    @Test
    public void isValid_UnequaledPassword_NotOk() {
        userRegistrationDto.setEmail("user@email.com");
        userRegistrationDto.setPassword("user1234");
        userRegistrationDto.setRepeatPassword("user12345678");
        Mockito.when(passwordConstraint.field()).thenReturn(PASSWORD_FIELD_NAME);
        Mockito.when(passwordConstraint.fieldMatch()).thenReturn(REPEAT_PASSWORD_FIELD_NAME);
        passwordValidator.initialize(passwordConstraint);
        boolean valid = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(valid, "Inputted passwords are the same. But must be not the same!");
    }

    @Test
    public void isValid_PasswordNull_NotOk() {
        userRegistrationDto.setEmail("user@email.com");
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword("user12345678");
        Mockito.when(passwordConstraint.field()).thenReturn(PASSWORD_FIELD_NAME);
        Mockito.when(passwordConstraint.fieldMatch()).thenReturn(REPEAT_PASSWORD_FIELD_NAME);
        passwordValidator.initialize(passwordConstraint);
        boolean valid = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(valid, "Inputted passwords are the same. But must be not the same!");
    }

    @Test
    public void isValid_RepeatedPasswordNull_NotOk() {
        userRegistrationDto.setEmail("user@email.com");
        userRegistrationDto.setPassword("user12345678");
        userRegistrationDto.setRepeatPassword(null);
        Mockito.when(passwordConstraint.field()).thenReturn(PASSWORD_FIELD_NAME);
        Mockito.when(passwordConstraint.fieldMatch()).thenReturn(REPEAT_PASSWORD_FIELD_NAME);
        passwordValidator.initialize(passwordConstraint);
        boolean valid = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(valid, "Inputted passwords are the same. But must be not the same!");
    }
}

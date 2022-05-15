package mate.academy.validation;

import java.lang.reflect.Field;
import mate.academy.model.dto.UserRegistrationDto;
import mate.academy.util.UserUtilForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private UserRegistrationDto userRegistrationDto;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserUtilForTest userUtil;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        userRegistrationDto = new UserRegistrationDto();
        userUtil = new UserUtilForTest();
        userRegistrationDto.setEmail(userUtil.getBorisEmail());
        userRegistrationDto.setPassword(userUtil.getBorisPassword());
        userRegistrationDto.setRepeatPassword(userUtil.getBorisPassword());
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        try {
            Class<PasswordValidator> passwordValidatorClass = PasswordValidator.class;
            Field field = passwordValidatorClass.getDeclaredField("field");
            Field fieldMatch = passwordValidatorClass.getDeclaredField("fieldMatch");
            field.setAccessible(true);
            fieldMatch.setAccessible(true);
            field.set(passwordValidator, "password");
            fieldMatch.set(passwordValidator, "repeatPassword");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Can't fill fields");
        }
    }

    @Test
    void isValid_ok() {
        boolean actual = passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_fieldsNotMatch_notOk() {
        userRegistrationDto.setRepeatPassword(userUtil.getNadjaPassword());
        boolean actual = passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_shortPassword_notOk() {
        userRegistrationDto.setPassword("");
        boolean actual = passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_nullPassword_notOk() {
        userRegistrationDto.setPassword(null);
        boolean actual = passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}
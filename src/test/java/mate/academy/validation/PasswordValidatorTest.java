package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

class PasswordValidatorTest {
    ConstraintValidator<Password, UserRegistrationDto> constraintValidator;
    ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void checkValidPasswords_Ok() throws NoSuchFieldException, IllegalAccessException {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword("1234");
        Class<? extends ConstraintValidator> constraintValidatorClass = constraintValidator.getClass();
        Field field = constraintValidatorClass.getDeclaredField("field");
        field.setAccessible(true);
        field.set(constraintValidator, "password");
        Field fieldMatch = constraintValidatorClass.getDeclaredField("fieldMatch");
        fieldMatch.setAccessible(true);
        fieldMatch.set(constraintValidator, "repeatPassword");
        boolean actual = constraintValidator.isValid(userRegistrationDto, constraintValidatorContext);
        boolean expected = true;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkNotValidPasswords_NotOk() throws NoSuchFieldException, IllegalAccessException {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword("12345");
        Class<? extends ConstraintValidator> constraintValidatorClass = constraintValidator.getClass();
        Field field = constraintValidatorClass.getDeclaredField("field");
        field.setAccessible(true);
        field.set(constraintValidator, "password");
        Field fieldMatch = constraintValidatorClass.getDeclaredField("fieldMatch");
        fieldMatch.setAccessible(true);
        fieldMatch.set(constraintValidator, "repeatPassword");
        boolean actual = constraintValidator.isValid(userRegistrationDto, constraintValidatorContext);
        boolean expected = false;
        Assertions.assertEquals(expected, actual);
    }
}
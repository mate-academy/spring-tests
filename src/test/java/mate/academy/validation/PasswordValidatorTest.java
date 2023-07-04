package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String VALIDATION_PASSWORD = "12345678";
    private static final String NOT_VALIDATION_PASSWORD = "1234";
    private UserRegistrationDto userRegistrationDto;
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext validatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        validatorContext = Mockito.mock(ConstraintValidatorContext.class);
        ReflectionTestUtils.setField(passwordValidator,"field","password");
        ReflectionTestUtils.setField(passwordValidator,"fieldMatch","repeatPassword");
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(VALIDATION_PASSWORD);
        userRegistrationDto.setRepeatPassword(VALIDATION_PASSWORD);
    }

    @Test
    void isValid_OK() {
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto,validatorContext));
    }

    @Test
    void isValid_Not_OK() {
        userRegistrationDto.setPassword(NOT_VALIDATION_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,validatorContext));
    }
}

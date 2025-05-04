package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String PASSWORD = "12345678";
    private static final String REPEAT_PASSWORD = "12345679";
    private UserRegistrationDto userRegistrationDto;
    private ConstraintValidatorContext constraintValidatorContext;
    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
        userRegistrationDto = new UserRegistrationDto();
    }

    @Test
    void isValid_SamePasswords_True() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(PASSWORD);
        passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
    }

    @Test
    void isValid_DifferentPasswords_True() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(REPEAT_PASSWORD);
        passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
    }
}

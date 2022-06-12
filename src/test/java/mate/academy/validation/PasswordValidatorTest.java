package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String PASS_REGULAR = "1234";
    private static final String PASS_NULL = null;
    private static final String PASS_DIFF = "4321";
    private static final PasswordValidator passwordValidator = new PasswordValidator();
    private static final ConstraintValidatorContext constraintValidatorContext =
            Mockito.mock(ConstraintValidatorContext.class);

    @BeforeAll
    static void beforeAll() {
        passwordValidator.initialize(UserRegistrationDto.class.getAnnotation(Password.class));
    }

    @Test
    void isValid_Ok() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setPassword(PASS_REGULAR);
        registrationDto.setRepeatPassword(PASS_REGULAR);
        assertTrue(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_Null_NotOk() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setPassword(PASS_NULL);
        registrationDto.setRepeatPassword(PASS_REGULAR);
        assertFalse(passwordValidator.isValid(registrationDto, constraintValidatorContext));
        registrationDto.setPassword(PASS_REGULAR);
        registrationDto.setRepeatPassword(PASS_NULL);
        assertFalse(passwordValidator.isValid(registrationDto, constraintValidatorContext));
        registrationDto.setPassword(PASS_NULL);
        registrationDto.setRepeatPassword(PASS_NULL);
        assertFalse(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_Diff_NotOk() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setPassword(PASS_REGULAR);
        registrationDto.setRepeatPassword(PASS_DIFF);
        assertFalse(passwordValidator.isValid(registrationDto, constraintValidatorContext));
        registrationDto.setPassword(PASS_DIFF);
        registrationDto.setRepeatPassword(PASS_REGULAR);
        assertFalse(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }
}
package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String TEST_PASSWORD = "987654321";
    private PasswordValidator passwordValidator;
    private UserRegistrationDto userRegistrationDto;
    private ConstraintValidatorContext constraintValidatorContext;
    private Password constraintAnnotation;
    
    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        userRegistrationDto = new UserRegistrationDto();
        constraintAnnotation = Mockito.mock(Password.class);
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }
    
    @Test
    void isValid_ok() {
        userRegistrationDto.setPassword(TEST_PASSWORD);
        userRegistrationDto.setRepeatPassword(TEST_PASSWORD);
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }
    
    @Test
    void isValid_PasswordOrRepeatPasswordIsNull_notOk() {
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
    
    @Test
    void isValid_PasswordAndRepeatPasswordNotMatch_notOk() {
        userRegistrationDto.setPassword(TEST_PASSWORD);
        userRegistrationDto.setRepeatPassword(TEST_PASSWORD + TEST_PASSWORD);
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}

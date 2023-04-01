package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {
    private static final String EMAIL = "den@gmail.com";
    private static final String PASS = "127836";
    private static final String MATCH_PASS = "127836";
    private static final String NOT_MATCH_PASS = "884739";
    private PasswordValidator passwordValidator;
    private UserRegistrationDto userRegistrationDto;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;
    @Mock
    private Password constraintAnnotation;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        userRegistrationDto = new UserRegistrationDto();
    }

    @Test
    void password_isValid() {
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setPassword(PASS);
        userRegistrationDto.setRepeatPassword(MATCH_PASS);
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void password_isNotValid() {
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setPassword(PASS);
        userRegistrationDto.setRepeatPassword(NOT_MATCH_PASS);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }
}

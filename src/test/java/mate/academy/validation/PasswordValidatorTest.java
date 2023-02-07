package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);

    }

    @Test
    void valid_Ok() {
        String password = "1234546";
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(password);
        userRegistrationDto.setRepeatPassword(password);
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }
}

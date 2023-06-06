package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static String PASSWORD = "testPass";
    private static String REPEATED_PASSWORD_INVALID = "invalidPass";
    private static PasswordValidator passwordValidator;
    private static UserRegistrationDto registrationDto;
    private static ConstraintValidatorContext context;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        registrationDto = new UserRegistrationDto();
        context = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);        
    }

    @Test
    void isValid_valid_ok() {
        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword(PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(registrationDto, context));       
    }
    
    @Test
    void isValid_notValid_notOk() {
        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword(REPEATED_PASSWORD_INVALID);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context));       
    }
    
    @Test
    void isValid_nullValue_notOk() {
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context));       
    }
}

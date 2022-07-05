package mate.academy.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private ConstraintValidator<Password, UserRegistrationDto> validator;
    private Password password;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
        password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        validator.initialize(password);
    }

    @Test
    void isValid_ok() {
        UserRegistrationDto firstRegistrationDto = new UserRegistrationDto();
        firstRegistrationDto.setPassword("123456789");
        firstRegistrationDto.setRepeatPassword("123456789");
        UserRegistrationDto secondRegistrationDto = new UserRegistrationDto();
        secondRegistrationDto.setPassword("23kj123klj12l3kj12lk3j12lk3j1l2k3j1l2kj3");
        secondRegistrationDto.setRepeatPassword("23kj123klj12l3kj12lk3j12lk3j1l2k3j1l2kj3");
        Assertions.assertTrue(validator.isValid(firstRegistrationDto, context));
        Assertions.assertTrue(validator.isValid(secondRegistrationDto, context));
    }

    @Test
    void isValid_differentPasswords_notOk() {
        UserRegistrationDto firstRegistrationDto = new UserRegistrationDto();
        firstRegistrationDto.setPassword("123456789");
        firstRegistrationDto.setRepeatPassword("987654321");
        UserRegistrationDto secondRegistrationDto = new UserRegistrationDto();
        secondRegistrationDto.setPassword("123456789");
        secondRegistrationDto.setRepeatPassword("");
        Assertions.assertFalse(validator.isValid(firstRegistrationDto, context));
        Assertions.assertFalse(validator.isValid(secondRegistrationDto, context));
    }
}

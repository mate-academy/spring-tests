package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    @InjectMocks
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
        ReflectionTestUtils.setField(passwordValidator, PasswordValidator.class,
                                     "field",
                                     "password", String.class);
        ReflectionTestUtils.setField(passwordValidator, PasswordValidator.class,
                                     "fieldMatch",
                                     "repeatPassword", String.class);
    }

    @Test
    void isValid_Ok() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("bob@i.ua");
        registrationDto.setPassword("1234");
        registrationDto.setRepeatPassword("1234");

        Assertions.assertTrue(passwordValidator.isValid(registrationDto, context));
    }

    @Test
    void isValid_NotMatchPasswords_False() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("bob@i.ua");
        registrationDto.setPassword("56789");
        registrationDto.setRepeatPassword("1234");

        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context));
    }

    @Test
    void isValid_NullRepeatPasswords_False() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("bob@i.ua");
        registrationDto.setPassword("56789");
        registrationDto.setRepeatPassword(null);

        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context));
    }

    @Test
    void isValid_NullPasswords_False() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("bob@i.ua");
        registrationDto.setPassword(null);
        registrationDto.setRepeatPassword(null);

        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context));
    }

}

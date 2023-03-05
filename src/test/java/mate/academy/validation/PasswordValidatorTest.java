package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private ConstraintValidatorContext contextMock;
    private PasswordValidator passwordValidator;
    private UserRegistrationDto registrationDto;
    private String password = "user1234";
    private String email = "user@gmail.com";

    @BeforeEach
    void setUp() {
        contextMock = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(email);
        registrationDto.setPassword(password);
        registrationDto.setRepeatPassword(password);
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
    }

    @Test
    void isValid_Ok() {
        boolean actual = passwordValidator.isValid(registrationDto, contextMock);
        assertTrue(actual);
    }

    @Test
    void isValid_DifferentRepeatPassword_Ok() {
        registrationDto.setRepeatPassword("user");
        boolean actual = passwordValidator.isValid(registrationDto, contextMock);
        assertFalse(actual);
    }

    @Test
    void isValid_DtoIsNull_NotOk() {
        assertThrows(RuntimeException.class, () -> {
            passwordValidator.isValid(null, contextMock);
        });
    }
}

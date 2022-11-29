package mate.academy.validation;

import javassist.tools.reflect.Reflection;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String PASS_1 = "1234";
    private static final String PASS_2 = "12345678";
    private PasswordValidator passwordValidator;
    private UserRegistrationDto dto;

    @BeforeEach
    void setUp() throws Exception {
        passwordValidator = new PasswordValidator();
        ReflectionTestUtils.setField(passwordValidator, "field", "password", String.class);
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword", String.class);
    }

    @Test
    void isValid_ok() {
        dto = new UserRegistrationDto();
        dto.setPassword(PASS_1);
        dto.setRepeatPassword(PASS_1);
        assertTrue(passwordValidator.isValid(dto, Mockito.mock(ConstraintValidatorContext.class)));
    }

    @Test
    void isValid_notOk() throws Exception {
        dto = new UserRegistrationDto();
        dto.setPassword(PASS_1);
        dto.setRepeatPassword(PASS_2);
        assertFalse(passwordValidator.isValid(dto, Mockito.mock(ConstraintValidatorContext.class)));
    }
}
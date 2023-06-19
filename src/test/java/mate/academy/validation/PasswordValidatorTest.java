package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String USER_PASSWORD = "password";
    private static final String INCORRECT_USER_PASSWORD = "incorrect.password";
    private PasswordValidator underTest;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        underTest = new PasswordValidator();
        constraintValidatorContext = mock(ConstraintValidatorContext.class);
        ReflectionTestUtils.setField(underTest, "field", "password");
        ReflectionTestUtils.setField(underTest, "fieldMatch", "repeatPassword");
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(USER_PASSWORD);
        userRegistrationDto.setRepeatPassword(USER_PASSWORD);
    }

    @Test
    void isValidTrue() {
        assertTrue(underTest
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValidFalse() {
        userRegistrationDto.setPassword(INCORRECT_USER_PASSWORD);
        assertFalse(underTest
                .isValid(userRegistrationDto, constraintValidatorContext));
    }
}

package mate.academy.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "12345678";
    private ConstraintValidator constraintValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        constraintValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContextImpl.class);
        ReflectionTestUtils.setField(constraintValidator, "field", "password");
        ReflectionTestUtils.setField(constraintValidator, "fieldMatch", "repeatPassword");
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(constraintValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_DifferentPasswords_NotOk() {
        userRegistrationDto.setPassword("87654321");
        Assertions.assertFalse(constraintValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }
}

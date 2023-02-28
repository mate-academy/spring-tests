package mate.academy.validation;

import java.util.HashMap;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.hibernate.validator.internal.util.annotation.AnnotationDescriptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PasswordValidatorTest {
    public static final String EMAIL = "modernboy349gmail.com";
    public static final String PASSWORD = "Hello123";
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(PASSWORD);
        HashMap<String, Object> fieldsMap = new HashMap<>();
        fieldsMap.put("field", "password");
        fieldsMap.put("fieldMatch", "repeatPassword");

        final PasswordValidator passwordValidator = new PasswordValidator();
        final Password password = new AnnotationDescriptor.Builder<>(Password.class,
                fieldsMap).build().getAnnotation();
        passwordValidator.initialize(password);
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_wrongRepeatPassword_NotOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword("");
        HashMap<String, Object> fieldsMap = new HashMap<>();
        fieldsMap.put("field", "password");
        fieldsMap.put("fieldMatch", "repeatPassword");

        final PasswordValidator passwordValidator = new PasswordValidator();
        final Password password = new AnnotationDescriptor.Builder<>(Password.class,
                fieldsMap).build().getAnnotation();
        passwordValidator.initialize(password);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }
}

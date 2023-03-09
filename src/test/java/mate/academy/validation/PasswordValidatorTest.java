package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "password";
    private static final String INVALID_PASSWORD = "repeatPassword";
    private UserRegistrationDto userRegistrationDto;
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator.initialize(getPasswordAnnotation("password", "repeatPassword"));
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(passwordValidator
                        .isValid(userRegistrationDto, constraintValidatorContext),
                String.format("Result should be true for password: %s, and repeat password: "
                        + "%s, but was false", VALID_PASSWORD, VALID_PASSWORD));
    }

    @Test
    void isValid_passwordIsNotEquals_Ok() {
        userRegistrationDto.setRepeatPassword(INVALID_PASSWORD);
        Assertions.assertFalse(passwordValidator
                        .isValid(userRegistrationDto, constraintValidatorContext),
                String.format("Result should be false for password: %s, and repeat password: "
                        + "%s, but was true", VALID_PASSWORD, INVALID_PASSWORD));
    }

    @Test
    void isValid_passwordIsNull_Ok() {
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
        Assertions.assertFalse(passwordValidator
                        .isValid(userRegistrationDto, constraintValidatorContext),
                "Result should be false for null password but was true");
    }

    private Password getPasswordAnnotation(String field, String fieldMatch) {
        return new Password() {
            @Override
            public Class<?>[] groups() {
                return new Class<?>[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return (Class<? extends Payload>[]) new Class<?>[0];
            }

            @Override
            public String message() {
                return "";
            }

            @Override
            public String fieldMatch() {
                return fieldMatch;
            }

            @Override
            public String field() {
                return field;
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return Password.class;
            }
        };
    }
}

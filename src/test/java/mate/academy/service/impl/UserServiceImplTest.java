package mate.academy.service.impl;

import static org.mockito.ArgumentMatchers.any;

import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(
                userDao,
                passwordEncoder
        );
    }

    @Test
    void save_validInput_ok() {
        User user = new User();
        String password = "Password";
        user.setPassword(password);
        Mockito.when(userDao.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        User actualUser = userService.save(user);
        Assertions.assertTrue(passwordEncoder.matches(password, actualUser.getPassword()),
                "It should encode password when saving user");
    }
}

package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import mate.academy.dao.UserDao;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String PASSWORD = "1234";
    private static UserServiceImpl userService;

    @BeforeAll
    static void beforeAll() {
        UserDao userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void saveUser_passwordNotEncoded_notOk() {
        User user = new User();
        user.setPassword(PASSWORD);
        userService.save(user);
        String actualUserPassword = user.getPassword();
        assertNotEquals(PASSWORD, actualUserPassword,
                "Expected to encode password while saving, before insertion to database");
    }
}

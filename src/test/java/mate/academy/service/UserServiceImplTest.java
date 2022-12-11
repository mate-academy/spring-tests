package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_checkPasswordEncoding_Ok() {
        String password = "12345678";
        User user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword(password);
        user.setRoles(Set.of(new Role(Role.RoleName.USER), new Role(Role.RoleName.ADMIN)));
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        assertEquals(user, actual);
        assertTrue(passwordEncoder.matches(password, actual.getPassword()));
    }

    @Test
    void findById() {
    }

    @Test
    void findByEmail() {
    }
}
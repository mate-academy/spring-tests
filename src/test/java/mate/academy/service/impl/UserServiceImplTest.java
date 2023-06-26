package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String USER_EMAIL = "bob@gmail.com";
    private static final String USER_PASS = "1234";
    private static final String INVALID_EMAIL = "obgmail.com";
    private UserDao userDao;
    private UserService userService;
    private User user;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASS);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void saveUser_Ok() {
        Mockito.when(userService.save(user)).thenReturn(user);
        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
        assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findByEMail_Ok() {
        Mockito.when(userDao.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        Optional<User> optional = userService.findByEmail(USER_EMAIL);
        assertTrue(optional.isPresent(),
                "method should return not empty Optional if User exist in database");
        assertEquals(optional.get().getEmail(), user.getEmail(),
                "method should return User with actual email");
        assertEquals(optional.get().getPassword(), user.getPassword(),
                "method should return User with actual password");
    }

    @Test
    void findByWrongEmail_notOk() {
        assertThrows(NoSuchElementException.class, () -> userDao.findByEmail(INVALID_EMAIL).get(),
                "Method Should throw NoSUchElementException");
    }
}

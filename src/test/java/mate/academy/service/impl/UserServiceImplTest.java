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
    private static final String EMAIL = "cat@gmail.com";
    private static final String PASSWORD = "12345678";
    private static final String INVALID_EMAIL = "catgmail.com";
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
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
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
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Optional<User> optional = userService.findByEmail(EMAIL);
        assertTrue(optional.isPresent(),
                "Method should return not empty Optional if User exist in database");
        assertEquals(optional.get().getEmail(), user.getEmail(),
                "Method should return User with actual email");
        assertEquals(optional.get().getPassword(), user.getPassword(),
                "Method should return User with actual password");
    }

    @Test
    void findByWrongEmail_notOk() {
        assertThrows(NoSuchElementException.class, () -> userDao.findByEmail(INVALID_EMAIL).get(),
                "Method Should throw NoSUchElementException");
    }
}

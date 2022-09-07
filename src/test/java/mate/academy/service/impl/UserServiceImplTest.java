package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static UserService userService;
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private static User user;

    @BeforeAll
    static void beforeAll() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
    }

    @Test
    void save_user_OK() {
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn("%1234@");
        Mockito.when(userDao.save(any(User.class))).thenReturn(user);
        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals("bob@i.ua", actual.getEmail());
        assertEquals("%1234@", actual.getPassword());
        assertEquals(1, actual.getRoles().size());
        assertEquals("ADMIN", actual.getRoles().stream()
                .findFirst().orElseThrow(
                        () -> new RuntimeException("Couldn't find expected user role")
                ).getRoleName().name());
    }

    @Test
    void findById() {
        Mockito.when(userDao.findById(any(Long.class))).thenReturn(Optional.of(user));
        User actual = userService.findById(1L).orElseThrow(
                () -> new RuntimeException("Couldn't find user by id"));
        assertNotNull(actual);
        assertEquals("bob@i.ua", actual.getEmail());
        assertEquals("1234", actual.getPassword());
        assertEquals(1, actual.getRoles().size());
        assertEquals("ADMIN", actual.getRoles().stream()
                .findFirst().orElseThrow(
                        () -> new RuntimeException("Couldn't find expected user role")
                ).getRoleName().name());
    }

    @Test
    void findByEmail() {
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User actual = userService.findByEmail(user.getEmail()).orElseThrow(
                () -> new RuntimeException("Couldn't find user by email"));
        assertNotNull(actual);
        assertEquals("bob@i.ua", actual.getEmail());
        assertEquals("1234", actual.getPassword());
        assertEquals(1, actual.getRoles().size());
        assertEquals("ADMIN", actual.getRoles().stream()
                .findFirst().orElseThrow(
                        () -> new RuntimeException("Couldn't find expected user role")
                ).getRoleName().name());
    }
}

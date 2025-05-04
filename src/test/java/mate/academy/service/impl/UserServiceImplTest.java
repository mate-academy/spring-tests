package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String EMAIL = "testmail@i.ua";
    private static final String PASSWORD = "12345";
    private static final long ID = 1L;
    private static final String ENCODER_PASS = "111111111";
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User(ID, EMAIL, PASSWORD, ROLES);
    }

    @Test
    void findById_NotOk() {
        Mockito.when(userDao.findById(ID)).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findById(8L);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findByEmail_NotOk() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findByEmail("falded@email");
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        User actual = userService.findByEmail(EMAIL).orElseThrow();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertEquals(ROLES, actual.getRoles());
    }
}

package mate.academy.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    public static final String EMAIL = "vvv@i.ua";
    public static final String PASSWORD = "12341234";
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        RoleName roleNameUser = RoleName.USER;
        Role userRole = new Role(roleNameUser);
        user.setRoles(Set.of(userRole));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(any())).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(any())).thenReturn(Optional.of(user));
        User actual = userService.findById(1L).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.of(user));
        User actual = userService.findByEmail(EMAIL).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }
}

package mate.academy.service;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static final String EMAIL = "USER_EMAIL@com.ua";
    private static final String PASSWORD = "USER_EMAIL@com.ua";
    private final Role role = new Role(Role.RoleName.USER);
    private UserDao userDao;
    private PasswordEncoder encoder;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        encoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, encoder);
        user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        Mockito.when(encoder.encode(PASSWORD)).thenReturn("ENCODED_PASSWORD");
        User saveUser = userService.save(user);
        Assertions.assertNotNull(saveUser);
        Assertions.assertEquals(1L, saveUser.getId());
        Assertions.assertEquals(EMAIL, saveUser.getEmail());
        String encodedPassword = saveUser.getPassword();
        Assertions.assertNotNull(encodedPassword);
        Assertions.assertEquals("ENCODED_PASSWORD", saveUser.getPassword());
    }

    @Test
    void findById_ok() {
        Long id = 1L;
        Mockito.when(userDao.findById(id)).thenReturn(Optional.of(user));
        Optional<User> optionalUser = userService.findById(id);
        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals(EMAIL, optionalUser.get().getEmail());
        Assertions.assertEquals(PASSWORD, optionalUser.get().getPassword());
        Assertions.assertEquals(id, optionalUser.get().getId());
    }

    @Test
    void findById_notExistId_notOk() {
        Long id = 999L;
        Mockito.when(userDao.findById(id)).thenReturn(Optional.empty());
        Optional<User> optionalUser = userService.findById(id);
        Assertions.assertFalse(optionalUser.isPresent());
    }

    @Test
    void findByEmail_ok() {
        String email = "USER_EMAIL@com.ua";
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
        Optional<User> optionalUser = userService.findByEmail(email);
        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals(email, optionalUser.get().getEmail());
        Assertions.assertEquals(PASSWORD, optionalUser.get().getPassword());
        Assertions.assertEquals(1L, optionalUser.get().getId());
    }

    @Test
    void findByEmail_incorrectData_notOk() {
        String email = "ADMIN@com.ua";
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.empty());
        Optional<User> optionalUser = userService.findByEmail(email);
        Assertions.assertFalse(optionalUser.isPresent());
    }
}

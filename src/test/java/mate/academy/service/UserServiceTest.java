package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {
    private static final String BOB_MAIL = "bob@mail.com";
    private static final String SIMPLE_PASSWORD = "1234";
    private static final long BOB_ID = 1L;
    private static UserService userService;
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private User bob;

    @BeforeAll
    static void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @BeforeEach
    void beforeEach() {
        bob = new User();
        bob.setEmail(BOB_MAIL);
        bob.setPassword(SIMPLE_PASSWORD);

        Mockito.when(userDao.save(bob)).thenReturn(bob);
        Mockito.when(userDao.findById(BOB_ID)).thenReturn(Optional.of(bob));
        Mockito.when(userDao.findByEmail(BOB_MAIL)).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.encode(SIMPLE_PASSWORD)).thenReturn(SIMPLE_PASSWORD);
    }

    @Test
    void save_ok() {
        User save = userService.save(bob);
        System.out.println(save.getPassword());
        Assertions.assertNotNull(save, "You must return User object, after saving");
    }

    @Test
    void findById_Ok() {
        assertEquals(bob, userService.findById(BOB_ID).get(),
                "You must return Optional of user with the same id");
    }

    @Test
    void findById_NotOk() {
        assertEquals(Optional.empty(), userService.findById(2L), "When you can't find user,"
                + " you must return empty Optional");
    }

    @Test
    void findByEmail_Ok() {
        assertEquals(bob, userService.findByEmail(BOB_MAIL).get(),
                "You must return Optional of user with the same email");
    }

    @Test
    void findByEmail_NotOk() {
        assertEquals(Optional.empty(), userService.findByEmail("non@existed.mail"),
                "When you can't find user, You must return empty Optional");
    }
}

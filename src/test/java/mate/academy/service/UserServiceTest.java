package mate.academy.service;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao,passwordEncoder);
    }

    @Test
    void save_Ok() {
        User bob = new User();
        bob.setId(1L);
        bob.setEmail("bob@gmail.com");
        bob.setPassword("123");

        Mockito.when(passwordEncoder.encode(bob.getPassword())).thenReturn("321");
        Mockito.when(userDao.save(bob)).thenReturn(bob);

        User actual = userService.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L,actual.getId());
    }

    @Test
    void findById_Ok() {
        User bob = new User();
        bob.setId(1L);
        bob.setEmail("bob@gmail.com");
        bob.setPassword("123");

        Mockito.when(userDao.findById(bob.getId())).thenReturn(Optional.of(bob));

        User actual = userService.findById(1L).get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L,actual.getId());
    }

    @Test
    void findById_Not_Existing_Id() {
        User bob = new User();
        bob.setId(1L);
        bob.setEmail("bob@gmail.com");
        bob.setPassword("123");

        Mockito.when(userDao.findById(bob.getId())).thenReturn(Optional.of(bob));

        Optional<User> actual = userService.findById(2L);

        Assertions.assertTrue(actual.isEmpty());

    }

    @Test
    void findByEmail_Ok() {
        User bob = new User();
        bob.setId(1L);
        bob.setEmail("bob@gmail.com");
        bob.setPassword("123");

        Mockito.when(userDao.findByEmail(bob.getEmail())).thenReturn(Optional.of(bob));

        User actual = userService.findByEmail("bob@gmail.com").get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L,actual.getId());
    }

    @Test
    void findByEmail_Not_Existing_Email() {
        User bob = new User();
        bob.setId(1L);
        bob.setEmail("bob@gmail.com");
        bob.setPassword("123");

        Mockito.when(userDao.findByEmail(bob.getEmail())).thenReturn(Optional.of(bob));

        Optional<User> actual = userService.findByEmail("alice@gmail.com");

        Assertions.assertTrue(actual.isEmpty());

    }
}

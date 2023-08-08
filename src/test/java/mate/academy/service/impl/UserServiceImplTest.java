package mate.academy.service.impl;

import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserDao userDao;
    private PasswordEncoder encoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, encoder);
    }

    @Test
    void save_Ok() {
        User user = new User();
        user.setPassword("1234");
        when(userDao.save(user)).thenReturn(user);
        Assertions.assertNotNull(userService.save(user));
    }

    @Test
    void findById_Ok() {
        User user = new User();
        user.setEmail("hello");
        user.setPassword("1234");
        user.setId(1L);
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> optionalUser = userService.findById(1L);
        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals("hello", optionalUser.get().getEmail());
    }

    @Test
    void findById_Not_Existing_User_Not_Ok() {
        Assertions.assertTrue(userService.findById(2L).isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        String email = "hola@gmail.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
        Assertions.assertTrue(userService.findByEmail(email).isPresent());
        Assertions.assertEquals(email, userService.findByEmail(email).get().getEmail());
    }

    @Test
    void findByEmail_Not_Existing_User_Not_Ok() {
        String email = "hola@gmail.com";
        Assertions.assertTrue(userService.findByEmail(email).isEmpty());
    }
}

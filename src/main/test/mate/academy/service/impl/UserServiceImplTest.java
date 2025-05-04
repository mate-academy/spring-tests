package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void save_validUser_Ok() {
        String email = "email@em.com";
        String password = "123123123";
        User userWithoutId = new User();
        userWithoutId.setEmail(email);
        userWithoutId.setPassword(password);
        User userWithIdAndEncodedPassword = new User();
        userWithIdAndEncodedPassword.setEmail(email);
        String encodedPassword = "*********";
        userWithIdAndEncodedPassword.setPassword(encodedPassword);
        userWithIdAndEncodedPassword.setId(178L);
        Mockito.when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        Mockito.when(userDao.save(userWithoutId)).thenReturn(userWithIdAndEncodedPassword);

        User actualUser = userService.save(userWithoutId);

        assertNotNull(actualUser);
        assertEquals(actualUser, userWithIdAndEncodedPassword);
    }

    @Test
    void findById_validId_Ok() {
        User expectedUser = new User();
        Long id = 72L;
        expectedUser.setEmail("email@em.com");
        expectedUser.setPassword("123123123");
        expectedUser.setId(id);
        Mockito.when(userDao.findById(id)).thenReturn(Optional.of(expectedUser));

        Optional<User> actualUserOptional = userService.findById(id);

        assertTrue(actualUserOptional.isPresent());
        User actualUser = actualUserOptional.get();
        assertEquals(actualUser, expectedUser);
    }

    @Test
    void findByEmail_validEmail_Ok() {
        User expectedUser = new User();
        String email = "email@em.com";
        expectedUser.setEmail(email);
        expectedUser.setPassword("123123123");
        expectedUser.setId(72L);
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        Optional<User> actualUserOptional = userService.findByEmail(email);

        assertTrue(actualUserOptional.isPresent());
        User actualUser = actualUserOptional.get();
        assertEquals(actualUser, expectedUser);
    }
}

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

import static org.mockito.ArgumentMatchers.any;

class UserServiceImplTest {
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);

        user = new User();
        user.setId(1L);
        user.setEmail("Bob@mail.com");
        user.setPassword("password");
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
    }

    @Test
    void save_Ok() {
        Mockito.when(passwordEncoder.encode((any()))).thenReturn("EncodePassword");
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
        Assertions.assertEquals(user.getEmail(),actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(user.getRoles().size(), actual.getRoles().size());
    }

    @Test
    void findById_existId_Ok() {
        long existId = user.getId();
        Mockito.when(userDao.findById(existId)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(existId);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(user.getId(), actual.get().getId());
        Assertions.assertEquals(user.getEmail(),actual.get().getEmail());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
        Assertions.assertEquals(user.getRoles().size(), actual.get().getRoles().size());
    }

    @Test
    void findById_nonExistId_NotOk() {
        long nonExistId = 5L;
        Mockito.when(userDao.findById(any())).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(nonExistId);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_existEmail_Ok() {
        String existEmail = user.getEmail();
        Mockito.when(userDao.findByEmail(existEmail)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(existEmail);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(user.getId(), actual.get().getId());
        Assertions.assertEquals(user.getEmail(),actual.get().getEmail());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
        Assertions.assertEquals(user.getRoles().size(), actual.get().getRoles().size());
    }

    @Test
    void findByEmail_nonExistEmail_NotOk() {
        String nonExistEmail = "alice@i.ua";
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(nonExistEmail);
        Assertions.assertTrue(actual.isEmpty());
    }
}

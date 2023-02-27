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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao,passwordEncoder);
    }

    @Test
    void save_Ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("12345678");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userDao.save(bob)).thenReturn(bob);
        User actual = userService.save(bob);
        Assertions.assertEquals(bob.getEmail(), actual.getEmail());
        Assertions.assertEquals(bob.getRoles(), actual.getRoles());
    }

    @Test
    void findById_Ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("12345678");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals("bob@i.ua",actual.get().getEmail());
    }

    @Test
    void findByEmail_Ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("12345678");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userDao.findByEmail("bob@i.ua")).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findByEmail("bob@i.ua");
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals("bob@i.ua",actual.get().getEmail());
    }
}

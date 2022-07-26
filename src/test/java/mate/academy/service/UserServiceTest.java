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
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User bob;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(bob)).thenReturn(bob);
        User actual = userService.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob.getEmail(), actual.getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(Mockito.anyLong())).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.get().getPassword());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(bob.getEmail())).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findByEmail(bob.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.get().getPassword());
    }
}

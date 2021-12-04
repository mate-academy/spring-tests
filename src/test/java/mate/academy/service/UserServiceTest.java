package mate.academy.service;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static User user;
    private static UserService userService;
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;

    @BeforeAll
    static void beforeAll() {
        user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("12342");
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao,passwordEncoder);
    }

    @Test
    void save() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user,actual);
    }

    @Test
    void findById() {
        Mockito.when(userDao.findById(user.getId()))
                .thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findById(user.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(Optional.of(user),actual);
    }

    @Test
    void findByEmail() {
        Mockito.when(userDao.findByEmail(user.getEmail()))
                .thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(Optional.of(user),actual);
    }
}

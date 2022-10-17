package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234Bob";
    private PasswordEncoder encoder;
    private UserService userService;
    private UserDao userDao;
    private RoleDao roleDao;
    private User bob;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        encoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, encoder);

        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);

        bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(encoder.encode(PASSWORD));
        bob.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userService.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
    }

    @Test
    void findById_Ok() {
        userService.save(bob);
        User actual = userService.findById(1L).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void findById_NotOk() {
        userService.save(bob);
        Optional<User> actual = userService.findById(2L);
        Assertions.assertTrue(actual.isEmpty());
        Assertions.assertEquals(Optional.empty(), actual);
    }

    @Test
    void findByEmail_Ok() {
        userService.save(bob);
        User actual = userService.findByEmail(EMAIL).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void findByEmail_NotOk() {
        userService.save(bob);
        Optional<User> actual = userService.findByEmail("alice@i.ua");
        Assertions.assertTrue(actual.isEmpty());
        Assertions.assertEquals(Optional.empty(), actual);
    }
}

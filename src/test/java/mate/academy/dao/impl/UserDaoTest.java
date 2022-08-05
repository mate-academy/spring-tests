package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;
    private User bob;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(Role.RoleName.ADMIN);
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(role);
        Set<Role> roles = Set.of(role);
        bob = getUser("Bob@gmail.com", "Qwerty!234", roles);
    }

    @Test
    void save_Ok() {
        User actualUser = userDao.save(bob);
        assertNotNull(actualUser);
        assertEquals(bob, actualUser);
    }

    @Test
    void save_notOk() {
        assertThrows(RuntimeException.class, () -> {
            userDao.save(null);
        });
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(bob);
        User actual = userDao.findByEmail(bob.getEmail()).get();
        assertNotNull(actual);
        assertEquals(bob.getEmail(), actual.getEmail());
    }

    @Test
    void findByEmail_notOk() {
        Optional<User> optionalUser = userDao.findByEmail("Asd");
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    void findById_Ok() {
        userDao.save(bob);
        Optional<User> actualOptionalUser = userDao.findById(1L);
        assertTrue(actualOptionalUser.isPresent());
        assertEquals(bob.getEmail(), actualOptionalUser.get().getEmail());
    }

    @Test
    void findById_notOk() {
        Optional<User> optionalUser = userDao.findById(-1L);
        assertTrue(optionalUser.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class, Role.RoleName.class};
    }

    private User getUser(String email, String password, Set<Role> roles) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        return user;
    }
}

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

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        Role adminRole = new Role(Role.RoleName.ADMIN);
        roleDao.save(adminRole);
        bob = getTestUser("Bob@gmail.com", "Qwerty!234", Set.of(adminRole));
    }

    @Test
    void save_validUser_Ok() {
        User actualUser = userDao.save(bob);
        assertNotNull(actualUser);
        assertEquals(bob, actualUser);
    }

    @Test
    void save_notPresent_notOk() {
        assertThrows(RuntimeException.class, () -> {
            userDao.save(null);
        });
    }

    @Test
    void findByEmail_validEmail_Ok() {
        userDao.save(bob);
        User actual = userDao.findByEmail(bob.getEmail()).get();
        assertNotNull(actual);
        assertEquals(bob.getEmail(), actual.getEmail());
    }

    @Test
    void findByEmail_notPresent_Ok() {
        Optional<User> optionalUser = userDao.findByEmail("Asd");
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    void findById_validId_Ok() {
        userDao.save(bob);
        Optional<User> actualOptionalUser = userDao.findById(1L);
        assertTrue(actualOptionalUser.isPresent());
        assertEquals(bob.getEmail(), actualOptionalUser.get().getEmail());
    }

    @Test
    void findById_notPresent_Ok() {
        Optional<User> optionalUser = userDao.findById(-1L);
        assertTrue(optionalUser.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class, Role.RoleName.class};
    }

    private User getTestUser(String email, String password, Set<Role> roles) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        return user;
    }
}

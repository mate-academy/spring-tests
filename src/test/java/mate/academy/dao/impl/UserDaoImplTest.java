package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {

        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void save_OK() {
        Role role = roleDao.save(new Role(Role.RoleName.USER));
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        bob.setRoles(Set.of(role));
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L,actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        Role role = roleDao.save(new Role(Role.RoleName.USER));

        String email = "bob@i.ua";
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword("1234");
        bob.setRoles(Set.of(role));

        User bobSaved = userDao.save(bob);
        Optional<User> actual = userDao.findByEmail(bobSaved.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(true,actual.isPresent());
        Assertions.assertTrue(bobSaved.getId().equals(actual.get().getId()));
        Assertions.assertTrue(bobSaved.getEmail().equals(actual.get().getEmail()));
    }
}

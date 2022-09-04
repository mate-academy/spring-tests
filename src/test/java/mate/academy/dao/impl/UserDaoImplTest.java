package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(Role.RoleName.ADMIN));
        roles.add(new Role(Role.RoleName.USER));
        user.setRoles(new HashSet<>(roles));
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(roles.get(0));
        roleDao.save(roles.get(1));
        userDao.save(user);
    }

    @Test
    void findByEmail_OK() {
        User actual = userDao.findByEmail(user.getEmail()).get();
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals("bob@i.ua", actual.getEmail());
        assertEquals("1234", actual.getPassword());
        assertEquals(2, actual.getRoles().size());
        assertEquals("USER", actual.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .filter(n -> n.equals("USER"))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("Couldn't find expected role name")));
    }

    @Test
    void findByEmail_notOK() {
        assertThrows(NoSuchElementException.class,() -> userDao.findByEmail("alice@i.ua").get());
    }
}

package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest{
    private UserDao userDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_validUser_Ok() {
        User expectedUser = new User();
        String email = "email@email.ok";
        String password = "12345678";
        Role role = roleDao.save(new Role(Role.RoleName.USER));

        expectedUser.setEmail(email);
        expectedUser.setPassword(password);
        expectedUser.setRoles(Set.of(role));
        User actualUser = userDao.save(expectedUser);
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void findByEmail_existingEmail_Ok() {
        User expectedUser = new User();
        String email = "email@email.ok";
        String password = "12345678";
        Role role = roleDao.save(new Role(Role.RoleName.USER));

        expectedUser.setEmail(email);
        expectedUser.setPassword(password);
        expectedUser.setRoles(Set.of(role));
        userDao.save(expectedUser);

        Optional<User> actualUserOptional = userDao.findByEmail(email);
        assertTrue(actualUserOptional.isPresent());
        User actualUser = actualUserOptional.get();
        assertEquals(expectedUser, actualUser);
    }
}
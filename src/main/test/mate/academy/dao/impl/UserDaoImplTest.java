package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest{
    private UserDao userDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void findByEmail_existingEmail_Ok() {
        User expectedUser = new User();
        String email = "email@email.ok";
        String password = "12345678";
        expectedUser.setEmail(email);
        expectedUser.setPassword(password);
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        User save = userDao.save(expectedUser);
        System.out.println(save);
        Optional<User> actualUserOptional = userDao.findByEmail(email);
        assertTrue(actualUserOptional.isPresent());
        User actualUser = actualUserOptional.get();
        assertEquals(expectedUser, actualUser);
    }
}
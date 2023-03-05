package mate.academy.dao.impl;

import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role admin;
    private Role user;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        admin = new Role(Role.RoleName.USER);
        user = new Role(Role.RoleName.USER);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @Test
    void save_Ok() {
        Role actualAdmin = roleDao.save(admin);
        Role actualUser = roleDao.save(user);
        Assertions.assertEquals(actualUser, user);
        Assertions.assertEquals(actualAdmin, admin);
    }

    @Test
    void save_Null_NotOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.save(null));
    }

    @Test
    void getByRoleName_Ok() {
        roleDao.save(user);
        Role actual = roleDao.getRoleByName("USER").get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.toString(), user.toString());
    }

    @Test
    void getByRoleName_Null_NotOk() {
        Assertions.assertThrows(Exception.class, () -> roleDao.save(null));
    }

    @Test
    void getByRoleName_WrongRole_NotOk() {
        roleDao.save(user);
        Assertions.assertThrows(DataProcessingException.class, () ->
                roleDao.getRoleByName("WRONG_NAME"));
    }
}

package mate.academy.dao.impl;

import mate.academy.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role user;
    private Role admin;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        user = new Role(Role.RoleName.USER);
        admin = new Role(Role.RoleName.ADMIN);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @Test
    void save_correctUser_Ok() {
        Role actual = roleDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_correctRoleName_Ok() {
        roleDao.save(user);
        Role actual = roleDao.getRoleByName("USER").get();
        Assertions.assertEquals(user.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_invalidRole_DataProcessingException() {
        roleDao.save(user);
        Assertions.assertThrows(DataProcessingException.class, () -> {
            roleDao.getRoleByName("user");
        });
    }
}

package mate.academy.dao.impl;

import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @Test
    void save_Ok() {
        Long expectedId = 1L;
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Role.RoleName expectedRoleName = role.getRoleName();
        Role actual = roleDao.save(role);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expectedId, actual.getId());
        Assertions.assertEquals(expectedRoleName, actual.getRoleName());
    }

    @Test
    void save_NotOk() {
        Role role = null;
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.save(role));
    }

    @Test
    void save_NotOk_Null_DataProcessingException() {
        Role role = null;
        try {
            roleDao.save(role);
        } catch (Exception e) {
            Assertions.assertEquals("Can't create entity: " + role, e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);

        Role actual = roleDao.getRoleByName("USER").get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_NotOk_Null() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName(null));
    }

    @Test
    void getRoleByName_NotOk_DataProcessingException() {
        String roleName = "JESUS";
        try {
            roleDao.getRoleByName(roleName);
        } catch (Exception e) {
            Assertions.assertEquals("Couldn't get role by role name: " + roleName, e.getMessage());
            return;
        }
        Assertions.fail();
    }
}

package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_role_OK() {
        Role actual = roleDao.save(new Role(Role.RoleName.ADMIN));
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("ADMIN", actual.getRoleName().name());
    }

    @Test
    void save_nullRole_notOK() {
        try {
            roleDao.save(null);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: null", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void getRoleByName_OK() {
        roleDao.save(new Role(Role.RoleName.ADMIN));
        Role actual = roleDao.getRoleByName(Role.RoleName.ADMIN.name()).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("ADMIN", actual.getRoleName().name());
    }

    @Test
    void getRoleByName_nullRole_notOK() {
        try {
            roleDao.getRoleByName(null);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: null", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }
}

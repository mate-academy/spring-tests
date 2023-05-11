package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private Role userRole;
    private Role adminRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = roleDao.save(new Role(Role.RoleName.USER));
        adminRole = roleDao.save(new Role(Role.RoleName.ADMIN));
    }

    @Test
    void save_saveRoles_Ok() {
        Assertions.assertEquals(1L, userRole.getId());
        Assertions.assertEquals(2L, adminRole.getId());
    }

    @Test
    void save_saveNullRole_NotOk() {
        Assertions.assertThrows(Exception.class, () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_Ok() {
        Role userRoleFromDb = roleDao.getRoleByName(userRole.getRoleName().name()).orElseThrow();
        assertRoleEquals(userRoleFromDb, userRole);

        Role adminRoleFromDb = roleDao.getRoleByName(adminRole.getRoleName().name()).orElseThrow();
        assertRoleEquals(adminRoleFromDb, adminRole);
    }

    @Test
    void getRoleByName_NullName_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null).orElseThrow());
    }

    private void assertRoleEquals(Role expected, Role actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName());
    }
}

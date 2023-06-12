package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final String MODERATOR_ROLE = "MODERATOR";
    private RoleDao roleDao;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        adminRole = roleDao.save(new Role(Role.RoleName.ADMIN));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @Test
    void save_saveRole_Ok() {
        //arrange
        Long expectedAdminId = 1L;

        //assert
        Assertions.assertEquals(expectedAdminId, adminRole.getId());
        Assertions.assertEquals(Role.RoleName.ADMIN, adminRole.getRoleName());
    }

    @Test
    void save_saveNull_NotOk() {
        //assert
        Assertions.assertThrows(Exception.class, () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_validName_Ok() {
        //act
        Role actualAdminRole = roleDao.getRoleByName(adminRole.getRoleName().name()).orElseThrow();

        //assert
        assertRoleEquals(adminRole, actualAdminRole);
    }

    @Test
    void getRoleByName_notValidName_NotOk() {
        //assert
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(MODERATOR_ROLE).orElseThrow());
    }

    @Test
    void getRoleByName_nullNameOk() {
        //assert
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null).orElseThrow());
    }

    private void assertRoleEquals(Role excepted, Role actual) {
        Assertions.assertEquals(excepted.getId(), actual.getId());
        Assertions.assertEquals(excepted.getRoleName(), actual.getRoleName());
    }
}

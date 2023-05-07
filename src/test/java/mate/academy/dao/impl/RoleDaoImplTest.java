package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static Role userRole;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeAll
    static void initRole() {
        userRole = new Role(Role.RoleName.USER);
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        Role adminRole = new Role(Role.RoleName.ADMIN);
        Assertions.assertEquals(1L, roleDao.save(adminRole).getId(), "Role should be add to DB");
        Assertions.assertEquals(2L, roleDao.save(userRole).getId(), "Role should be add to DB");
    }

    @Test
    void save_saveNullRole_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_correctName_ok() {
        Role adminRole = new Role(Role.RoleName.ADMIN);
        roleDao.save(adminRole);
        Assertions.assertEquals(adminRole.getId(),
                roleDao.getRoleByName(Role.RoleName.ADMIN.name())
                .get().getId(), "The role should be obtained");
        Assertions.assertEquals(adminRole.getRoleName(),
                roleDao.getRoleByName(Role.RoleName.ADMIN.name())
                .get().getRoleName(), "The role should be obtained");
    }

    @Test
    void getRoleByName_incorrectName_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("ADMN").get(),
                "Expected: DataProcessingException for incorrect name");
    }

    @Test
    void getRoleByName_addNullRole_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.save(null), "Role can't be null");
    }
}

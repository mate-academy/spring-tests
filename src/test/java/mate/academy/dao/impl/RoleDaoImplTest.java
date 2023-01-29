package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static String ROLE_NAME_USER = "USER";
    private static String ROLE_NAME_ADMIN = "ADMIN";
    private static String ROLE_NAME_NOT_EXIST = "MODERATOR";
    private static Role userRole;
    private static Role adminRole;
    private RoleDao roleDao;

    @BeforeAll
    static void beforeAll() {
        userRole = new Role(Role.RoleName.USER);
        adminRole = new Role(Role.RoleName.ADMIN);
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_adminRole_Ok() {
        Role savedRole = roleDao.save(adminRole);
        Assertions.assertEquals(1L, savedRole.getId());
    }

    @Test
    void save_roleNull_dataProcessingException_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_existentRole_Ok() {
        Role savedRole = roleDao.save(adminRole);
        Optional<Role> admin = roleDao.getRoleByName(ROLE_NAME_ADMIN);
        Assertions.assertTrue(admin.isPresent());
    }

    @Test
    void getRoleByName_notExistentRoleInDb_optionalEmpty() {
        Role savedRole = roleDao.save(adminRole);
        Optional<Role> role = roleDao.getRoleByName(ROLE_NAME_USER);
        Assertions.assertTrue(role.isEmpty());
    }

    @Test
    void getRoleByName_notExistentRole_dataProcessingException() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(ROLE_NAME_NOT_EXIST));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}

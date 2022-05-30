package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role adminRole;
    private Role userRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        adminRole = new Role(Role.RoleName.ADMIN);
        userRole = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actualAdmin = roleDao.save(adminRole);
        Role actualUser = roleDao.save(userRole);
        Assertions.assertNotNull(actualAdmin);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(1L, actualAdmin.getId());
        Assertions.assertEquals(2L, actualUser.getId());
        Assertions.assertEquals(adminRole.getRoleName(), actualAdmin.getRoleName());
        Assertions.assertEquals(userRole.getRoleName(), actualUser.getRoleName());
    }

    @Test
    void getRoleByName_existRole_Ok() {
        roleDao.save(adminRole);
        String existRole = adminRole.getRoleName().name();
        Optional<Role> actual = roleDao.getRoleByName(existRole);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(adminRole.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_notExistRole_Ok() {
        roleDao.save(adminRole);
        roleDao.save(userRole);
        String notExistRole = "MODERATOR";
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(notExistRole),
                "Expected to receive DataProcessingException "
                        + "while get role by non-exist roleName.\n");
    }
}

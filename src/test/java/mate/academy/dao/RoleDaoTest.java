package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private Role roleUser;
    private Role roleAdmin;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleUser = new Role();
        roleUser.setRoleName(Role.RoleName.USER);
        roleAdmin = new Role();
        roleAdmin.setRoleName(Role.RoleName.ADMIN);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(roleUser);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(roleUser.getRoleName(), actual.getRoleName());
    }

    @Test
    void save_NullRole_ThrowException() {
        try {
            roleDao.save(null);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: " + null, e.getMessage());
            return;
        }
        Assertions.fail("Incorrect role should throw DataProcessingException");
    }

    @Test
    void getRoleByName_Ok() {
        String roleName = "USER";
        roleDao.save(roleUser);
        roleDao.save(roleAdmin);
        Role actual = roleDao.getRoleByName(roleName).orElse(new Role());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(roleUser.getId(), actual.getId());
        Assertions.assertEquals(roleUser.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_NotExist_NotOk() {
        String roleName = "USER";
        roleDao.save(roleAdmin);
        Role actual = roleDao.getRoleByName(roleName).orElse(null);
        Assertions.assertNull(actual);
    }

    @Test
    void getRoleByName_NullRoleName_ThrowException() {
        try {
            roleDao.getRoleByName(null);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: "
                                            + null, e.getMessage());
            return;
        }
        Assertions.fail("Unavailable roleName should throw DataProcessingException");
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}

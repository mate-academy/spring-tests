package mate.academy.dao;

import mate.academy.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private Role roleUser;
    private Role roleAdmin;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleUser = new Role(Role.RoleName.USER);
        roleAdmin = new Role(Role.RoleName.ADMIN);
    }

    @Test
    void save_ok() {
        Role actual = roleDao.save(roleUser);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(Role.RoleName.USER.name(), actual.getRoleName().name());
    }

    @Test
    void save_roleIsNull_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_roleExists_ok() {
        String adminRole = Role.RoleName.ADMIN.name();
        roleDao.save(roleAdmin);
        Role actual = roleDao.getRoleByName(adminRole).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(adminRole, actual.getRoleName().name());
    }

    @Test
    void getRoleByName_roleDoesntExist_notOk() {
        String testerRole = "TESTER";
        roleDao.save(roleAdmin);
        roleDao.save(roleUser);
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(testerRole));
    }

    @Test
    void getRoleByName_roleNull_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName(null));
    }
}

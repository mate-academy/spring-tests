package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private Role roleUser;
    private Role roleAdmin;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleUser = new Role(Role.RoleName.USER);
        roleAdmin = new Role(Role.RoleName.ADMIN);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(roleUser);
        assertNotNull(actual);
        assertEquals(Role.RoleName.USER.name(), actual.getRoleName().name());
    }

    @Test
    void save_rollIsNull_notOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_Ok() {
        String adminRole = "ADMIN";
        roleDao.save(roleAdmin);
        Role actual = roleDao.getRoleByName(adminRole).get();
        assertNotNull(actual);
        assertEquals(adminRole, actual.getRoleName().name());
    }

    @Test
    void getRoleByName_roleNotExist_NotOk() {
        String guestRole = "GUEST";
        roleDao.save(roleAdmin);
        roleDao.save(roleUser);
        assertThrows(DataProcessingException.class,() -> roleDao.getRoleByName(guestRole));
    }

    @Test
    void getRoleByName_null_NotOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName(null));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }
}

package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(Role.RoleName.USER, actual.getRoleName());
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void saveNull_NotOk_DataProcessingException() {
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
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_NotOk_DataProcessingException() {
        String roleName = "DEMO";
        try {
            roleDao.getRoleByName(roleName);
        } catch (Exception e) {
            Assertions.assertEquals("Couldn't get role by role name: " + roleName, e.getMessage());
            return;
        }
        Assertions.fail();
    }
}

package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final String ROLE = Role.RoleName.USER.name();
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role actual = createRoleAndSave();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        createRoleAndSave();
        Optional<Role> actual = roleDao.getRoleByName(ROLE);
        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(Role.RoleName.valueOf(ROLE), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_invalidName_notOk() {
        String invalidRole = "felnfanfa";
        try {
            Optional<Role> actual = roleDao.getRoleByName(invalidRole);
            Assertions.assertTrue(actual.isEmpty());
        } catch (DataProcessingException e) {
            Assertions.assertEquals(
                    "Couldn't get role by role name: " + invalidRole, e.getMessage());
            return;
        }
        Assertions.fail("getRoleByName() is expected to throw DataProcessingException");
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    private Role createRoleAndSave() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.valueOf(ROLE));
        return roleDao.save(role);
    }
}

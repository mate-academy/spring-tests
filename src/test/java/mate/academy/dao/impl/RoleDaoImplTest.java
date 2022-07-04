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

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @Test
    void save_ok() {
        Role role = new Role(Role.RoleName.USER);
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, role.getId());
    }

    @Test
    void save_nullInput_notOk() {
        try {
            roleDao.save(null);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: null", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void getRoleByName_ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(actual.get(), role);
    }

    @Test
    void getRoleByName_wrongName_notOk() {
        String wrongName = "USOR";
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        try {
            roleDao.getRoleByName(wrongName);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: "
                            + wrongName, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }
}
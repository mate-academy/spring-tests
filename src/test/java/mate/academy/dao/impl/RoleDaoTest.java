package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final String ADMIN = Role.RoleName.ADMIN.name();
    private static final String NotPresentRole = "USR";
    private RoleDao roleDao;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);
    }

    @Test
    void save_validRole_Ok() {
        Role actual = roleDao.save(adminRole);
        assertEquals(adminRole.getRoleName(), actual.getRoleName());
    }

    @Test
    void save_notPresent_notOk() {
        assertThrows(RuntimeException.class, () -> {
            roleDao.save(new Role(Role.RoleName.valueOf(NotPresentRole)));
        });
    }

    @Test
    void getRoleByName_validName_Ok() {
        roleDao.save(adminRole);
        Role actual = roleDao.getRoleByName(ADMIN).get();
        assertNotNull(actual);
        assertEquals(adminRole.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_notPresent_notOk() {
        assertThrows(RuntimeException.class, () -> {
            roleDao.getRoleByName(NotPresentRole);
        });
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class, Role.RoleName.class};
    }
}

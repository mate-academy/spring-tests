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
    private static final String isNotPresentRole = "USR";
    private static RoleDao roleDao;
    private static Role roleTestAdmin;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleTestAdmin = new Role();
        roleTestAdmin.setRoleName(Role.RoleName.ADMIN);
    }

    @Test
    void save_validRole_Ok() {
        Role actual = roleDao.save(roleTestAdmin);
        assertEquals(roleTestAdmin.getRoleName(), actual.getRoleName());
    }

    @Test
    void save_notPresent_notOk() {
        assertThrows(RuntimeException.class, () -> {
            roleDao.save(new Role(Role.RoleName.valueOf(isNotPresentRole)));
        });
    }

    @Test
    void getRoleByName_validName_Ok() {
        roleDao.save(roleTestAdmin);
        Role actual = roleDao.getRoleByName(ADMIN).get();
        assertNotNull(actual);
        assertEquals(roleTestAdmin.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_notPresent_notOk() {
        assertThrows(RuntimeException.class, () -> {
            roleDao.getRoleByName(isNotPresentRole);
        });
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class, Role.RoleName.class};
    }
}

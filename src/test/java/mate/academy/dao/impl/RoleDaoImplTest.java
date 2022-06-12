package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoleDaoImplTest extends AbstractTest{
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private static final Role ADMIN_ROLE = new Role(Role.RoleName.ADMIN);
    private final RoleDao roleDao = new RoleDaoImpl(getSessionFactory());


    @Test
    void getRoleByName_Ok() {
        roleDao.save(USER_ROLE);
        roleDao.save(ADMIN_ROLE);
        Optional<Role> actualRole = roleDao.getRoleByName(Role.RoleName.USER.name());
        assertNotNull(actualRole);
        assertTrue(actualRole.isPresent());
        assertEquals(Role.RoleName.USER, actualRole.get().getRoleName());

        actualRole = roleDao.getRoleByName(Role.RoleName.ADMIN.name());
        assertNotNull(actualRole);
        assertTrue(actualRole.isPresent());
        assertEquals(Role.RoleName.ADMIN, actualRole.get().getRoleName());
    }

    @Override
    protected Class<?>[] entities() {
        Class[] classes = {Role.class};
        return classes;
    }
}
package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_validRole_Ok() {
        Role expectedRole = new Role(Role.RoleName.USER);
        Role actualRole = roleDao.save(expectedRole);
        assertNotNull(actualRole);
        assertEquals(expectedRole, actualRole);
    }

    @Test
    void getRoleByName_validName_Ok() {
        Role expectedRole = new Role(Role.RoleName.USER);
        roleDao.save(expectedRole);
        Optional<Role> actualOptionalRole = roleDao.getRoleByName("USER");
        assertTrue(actualOptionalRole.isPresent());
        assertEquals(expectedRole, actualOptionalRole.get());
    }
}
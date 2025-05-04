package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);

        assertNotNull(actual);
        assertEquals(role, actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void findByRoleName_OK() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(
                role.getRoleName().name());

        assertTrue(actual.isPresent());
        assertEquals(
                Role.RoleName.USER.name(), actual.get().getRoleName().name());
    }
}

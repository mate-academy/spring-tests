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

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role actual = createRole();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRollName_validRollName_Ok() {
        Role actual = createRole();
        Optional<Role> expected = roleDao.getRoleByName(actual.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.get().getRoleName(), actual.getRoleName());
    }

    @Test
    void getRollName_invalidRollName_Ok() {
        Assertions.assertThrows(DataProcessingException.class, () ->
                roleDao.getRoleByName(null));
    }

    private Role createRole() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        return roleDao.save(role);
    }
}

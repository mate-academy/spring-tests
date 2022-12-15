package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final Role.RoleName ROLE = Role.RoleName.USER;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(ROLE);
    }

    @Test
    void save_Ok() {
        Long expectedId = 1L;

        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expectedId, actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        Role expected = roleDao.save(role);
        Assertions.assertEquals(expected.getRoleName(),
                roleDao.getRoleByName(expected.getRoleName().name()).get().getRoleName());
    }

    @Test
    void getRoleByName_NotOk() {
        Assertions.assertEquals(Optional.empty(), roleDao.getRoleByName(ROLE.name()));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}

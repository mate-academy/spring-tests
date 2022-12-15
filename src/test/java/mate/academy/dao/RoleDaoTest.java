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
        Long id = 1L;
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual, "Method must return Role object");
        Assertions.assertEquals(id, actual.getId(),
                "Expected object with id " + id + ", but was " + actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        Role.RoleName expected = roleDao.save(role).getRoleName();
        Role.RoleName actual = roleDao.getRoleByName(expected.name()).get().getRoleName();
        Assertions.assertEquals(expected, actual,
                "Expected " + expected.name() + ", but was " + actual.name());
    }

    @Test
    void getRoleByName_NotOk() {
        Optional<Role> actual = roleDao.getRoleByName(ROLE.name());
        Assertions.assertEquals(Optional.empty(), actual,
                "Expected null value, but was " + actual);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}

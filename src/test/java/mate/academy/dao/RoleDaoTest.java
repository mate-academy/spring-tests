package mate.academy.dao;

import static org.assertj.core.api.Assertions.assertThat;

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
        Long expectedRoleId = 1L;
        assertThat(roleDao.getRoleByName(ROLE.name())).isEmpty();
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual, "Method must return Role object");
        Assertions.assertEquals(expectedRoleId, actual.getId(),
                "Expected object with id " + expectedRoleId + ", but was " + actual.getId());
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
        assertThat(actual).isEmpty();
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}

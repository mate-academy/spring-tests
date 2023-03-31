package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractDaoTest {
    private static final String USER_ROLE = "USER";
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void getRoleNyName_ok() {
        Role role = new Role(Role.RoleName.USER);
        Role expected = roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(USER_ROLE);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getRoleName(), actual.get().getRoleName(),
                "Expect actual role");
    }
}

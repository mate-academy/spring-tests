package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final Role.RoleName USER_ROLE = Role.RoleName.USER;
    private static final String USER_NAME = "USER";

    private Role role;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(USER_ROLE);
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void getRoleByName_ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(USER_NAME);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_roleIsNull_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null));
    }

    @Test
    void save_ok() {
        Role actual = roleDao.save(role);
        Assertions.assertEquals(actual.getId(), role.getId());
        Assertions.assertNotNull(actual);
    }
}

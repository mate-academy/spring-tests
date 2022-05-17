package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role expected;
    private static final Role.RoleName CORRECT_ROLE_NAME = Role.RoleName.USER;
    private static final String INVALID_ROLE_NAME = "UsEr";

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());

        expected = new Role();
        expected.setRoleName(CORRECT_ROLE_NAME);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(expected);

        Optional<Role> optionalRole = roleDao.getRoleByName(CORRECT_ROLE_NAME.name());
        if (optionalRole.isEmpty()) {
            Assertions.fail("Optional must be present");
        }

        Role actual = optionalRole.get();
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_DataProcessingException() {
        try {
            roleDao.getRoleByName(INVALID_ROLE_NAME);
        } catch (DataProcessingException e) {
            return;
        }
        Assertions.fail("DataProcessingException must be thrown");
    }
}

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
    private static final Long FIRST_INDEX_ID = 1L;
    private static final String INVALID_ROLE = "OWNER";
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @Test
    void save_validRole_ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(FIRST_INDEX_ID, actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_validRoleByName_ok() {
        roleDao.save(role);
        Optional<Role> actualOptional = roleDao.getRoleByName(role.getRoleName().name());
        Assertions.assertTrue(actualOptional.isPresent());
        Assertions.assertEquals(role.getRoleName(), actualOptional.get().getRoleName());
    }

    @Test
    void getRoleByName_invalidRole_notOk() {
        try {
            roleDao.getRoleByName(INVALID_ROLE);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: "
                    + INVALID_ROLE, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }
}

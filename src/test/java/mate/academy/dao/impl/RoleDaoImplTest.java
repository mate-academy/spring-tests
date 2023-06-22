package mate.academy.dao.impl;

import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final long EXISTED_ID = 1L;
    private static final String EXISTED_ROLE_NAME = "USER";
    private static final String NOT_EXISTED_ROLE_NAME = "ADMIN";
    private static final String NOT_VALID_ROLE_NAME = "NOT_VALID";
    private RoleDao roleDao;
    private Role testRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        testRole = new Role(USER);
    }

    @Test
    void saveRole_validValue_ok() {
        Role actual = roleDao.save(testRole);
        assertNotNull(actual,
                "method should not return null with valid Role value");
        assertEquals(EXISTED_ID, actual.getId(),
                "method should return Role id from database");
        assertEquals(testRole.getRoleName(), actual.getRoleName(),
                "method should return Role with saved in database name");
    }

    @Test
    void saveRole_nullValue_notOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.save(null),
                "method should throw DataProcessingException if Role is null");
    }

    @Test
    void getRoleByName_validRoleName_ok() {
        roleDao.save(testRole);
        Optional<Role> actual = roleDao.getRoleByName(EXISTED_ROLE_NAME);
        assertNotNull(actual,
                "method should not return null if Role name is valid");
        assertTrue(actual.isPresent(),
                "method should return not empty Optional if Role name is valid");
        assertNotNull(actual.get(),
                "method should return Optional with actual Role if Role name is valid");
        assertEquals(EXISTED_ID, actual.get().getId(),
                "method should return actual Role id");
        assertEquals(testRole.getRoleName(), actual.get().getRoleName(),
                "method should return actual Role name");
    }

    @Test
    void getRoleByName_notValidRoleNameOrNull_notOk() {
        roleDao.save(testRole);
        assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(NOT_VALID_ROLE_NAME),
                "method should throw DataProcessingException when Role name is not valid");
        assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null),
                "method should throw DataProcessingException when Role name is null");
    }

    @Test
    void getRoleByName_noRoleInDatabase_notOk() {
        roleDao.save(testRole);
        Optional<Role> actual = roleDao.getRoleByName(NOT_EXISTED_ROLE_NAME);
        assertTrue(actual.isEmpty(),
                "method should return empty Optional "
                         + "if Role name is valid but absent in database");
    }
}

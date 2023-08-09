package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final String NAME = "USER";
    private static final String INVALID_ROLE_NAME = "NOTUSER";
    private RoleDao roleDao;
    private Role testRole;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        testRole = new Role(Role.RoleName.USER);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(testRole);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(actual.getRoleName(), testRole.getRoleName());
    }

    @Test
    void save_NullRole_notOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.save(null),
                "Method should Throw DataProcessingException if Role null");
    }

    @Test
    void findByRoleName_Ok() {
        Role expected = roleDao.save(testRole);
        Optional<Role> actual = roleDao.getRoleByName(NAME);
        assertTrue(actual.isPresent(),
                "Method should return not empty Optional if Role exist in database");
        assertEquals(expected.getRoleName(), actual.get().getRoleName(),
                "Method should return User with actual email");
    }

    @Test
    void findByRoleName_WrongName_notOk() {
        assertThrows(DataProcessingException.class, () ->
                        roleDao.getRoleByName(INVALID_ROLE_NAME).get(),
                "Method Should throw DataProcessingException");
    }
}

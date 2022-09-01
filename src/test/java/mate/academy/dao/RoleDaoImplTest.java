package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @Test
    void save_ok() {
        Role expected = new Role(Role.RoleName.USER);
        Role actual = roleDao.save(expected);
        assertEquals(expected.getRoleName().name(), actual.getRoleName().name());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void save_roleIsNull_notOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.save(null),
                "Should receive DataProcessingException");
    }

    @Test
    void getRoleByName_ok() {
        Role expected = roleDao.save(new Role(Role.RoleName.USER));
        Optional<Role> actual = roleDao.getRoleByName("USER");
        assertTrue(actual.isPresent());
        assertEquals(expected.getRoleName(), actual.get().getRoleName());
        assertEquals(expected.getId(), actual.get().getId());
    }

    @Test
    void getRoleByName_notExistingRole_notOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName("DOG"),
                "Should receive DataProcessingException");
    }
}

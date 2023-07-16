package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.AbstractDaoTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractDaoTest {
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_validRole_ok() {
        Role actual = roleDao.save(role);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(Role.RoleName.USER, actual.getRoleName());
    }

    @Test
    void save_roleIsNull_notOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_roleFound_ok() {
        Role saved = roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(saved.getRoleName().name());
        assertTrue(actual.isPresent());
        assertEquals(saved.getId(), actual.get().getId());
        assertEquals(saved.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_roleNotFound_ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.ADMIN.name());
        assertTrue(actual.isEmpty());
    }

    @Test
    void getRoleByName_invalidRoleName_notOk() {
        assertThrows(
                DataProcessingException.class,
                () -> roleDao.getRoleByName("DefinitelyInvalidRoleName")
        );
    }

    @Test
    void getRoleByName_roleNameIsNull_notOk() {
        assertThrows(
                DataProcessingException.class,
                () -> roleDao.getRoleByName(null)
        );
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }
}

package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final Role.RoleName ROLE_NAME = Role.RoleName.ADMIN;
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(ROLE_NAME);
    }

    @Test
    void save_ok() {
        Role actual = roleDao.save(role);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_existedRole_ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(ROLE_NAME.name());
        assertFalse(actual.isEmpty());
        assertEquals(Role.RoleName.valueOf(ROLE_NAME.name()), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_notExistedRole_notOk() {
        assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null));
    }
}

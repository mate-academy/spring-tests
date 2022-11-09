package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role userRole;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role(Role.RoleName.USER);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(userRole);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void save_nullValue_NotOk() {
        assertThrows(DataProcessingException.class,
                () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(userRole);
        Optional<Role> actual = roleDao.getRoleByName("USER");
        assertNotNull(actual);
        assertEquals(userRole.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_emptyRoleName_NotOk() {
        assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(""));
    }
}

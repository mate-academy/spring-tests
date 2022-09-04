package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(new Role(Role.RoleName.ADMIN));
    }

    @Test
    void getRoleByName_OK() {
        Role actual = roleDao.getRoleByName(Role.RoleName.ADMIN.name()).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("ADMIN", actual.getRoleName().name());
    }

    @Test
    void getRoleByName_notOK() {
        assertThrows(NoSuchElementException.class,
                () -> roleDao.getRoleByName(Role.RoleName.USER.name()).get());
    }
}

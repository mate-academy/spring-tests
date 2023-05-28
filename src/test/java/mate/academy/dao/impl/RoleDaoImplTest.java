package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_validRole_ok() {
        Role expected = new Role(Role.RoleName.USER);
        Role actual = roleDao.save(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_validRole_ok() {
        Role expected = new Role();
        expected.setRoleName(Role.RoleName.USER);
        expected = roleDao.save(expected);
        Optional<Role> actual = roleDao.getRoleByName(expected.getRoleName().name());

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(expected.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_invalidRoles_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("NotExistedRole"));
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null));
    }
}

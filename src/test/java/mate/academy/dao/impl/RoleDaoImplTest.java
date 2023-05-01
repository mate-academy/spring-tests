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

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class<?>[]{Role.class};
    }

    @Test
    void save_Ok() {
        Role role = new Role(Role.RoleName.USER);
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, role.getId());
    }

    @Test
    void save_RoleNameIsNull_NotOk() {
        DataProcessingException dataProcessingExceptionExpected =
                Assertions.assertThrows(DataProcessingException.class, () -> {
                    roleDao.save(null);
                }, "DataProcessingException expected");
        Assertions.assertEquals("Can't create entity: null",
                dataProcessingExceptionExpected.getLocalizedMessage());
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Optional<Role> roleByName = roleDao.getRoleByName("USER");
        Assertions.assertTrue(roleByName.isPresent());
        Role actual = roleByName.get();
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_RoleNull_NotOk() {
        DataProcessingException dataProcessingExceptionExpected
                = Assertions.assertThrows(DataProcessingException.class, () -> {
                    roleDao.getRoleByName(null);
                }, "DataProcessingException expected");
        Assertions.assertEquals("Couldn't get role by role name: null",
                dataProcessingExceptionExpected.getLocalizedMessage());
    }
}

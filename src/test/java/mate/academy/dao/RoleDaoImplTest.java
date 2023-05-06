package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
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
        return new Class[]{Role.class};
    }

    @Test
    void save_ok() {
        Role role = new Role(Role.RoleName.USER);
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void save_roleNameIsNull_notOk() {
        Throwable exception = Assertions.assertThrows(DataProcessingException.class, () -> {
            roleDao.save(null);
        }, "DataProcessingException was expected");
        Assertions.assertEquals("Can't create entity: null", exception.getLocalizedMessage());
    }

    @Test
    void getRoleByName_ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Optional<Role> roleOptional = roleDao.getRoleByName("USER");
        Assertions.assertTrue(roleOptional.isPresent());
        Role actual = roleOptional.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_roleNameIsNull_notOk() {
        Throwable exception = Assertions.assertThrows(DataProcessingException.class, () -> {
            roleDao.getRoleByName(null);
        }, "DataProcessingException was expected .");
        Assertions.assertEquals("Couldn't get role by role name: null",
                exception.getLocalizedMessage());
    }
}

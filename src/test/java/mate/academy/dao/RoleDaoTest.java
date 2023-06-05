package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(Role.RoleName.USER,actual.getRoleName());
    }

    @Test
    void save_RoleNull_NotOk() {
        DataProcessingException dataProcessingExceptionExpected =
                Assertions.assertThrows(DataProcessingException.class,
                        () -> roleDao.save(null), "DataProcessingException expected");
        Assertions.assertEquals("Can't create entity: null",
                dataProcessingExceptionExpected.getMessage());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> roleOptional = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertTrue(roleOptional.isPresent());
        Role actual = roleOptional.get();
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_RollNull_Ok() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null),
                "DataProcessingException expected");
    }
}

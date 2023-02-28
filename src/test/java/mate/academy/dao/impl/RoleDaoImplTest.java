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
        return new Class[] { Role.class};
    }

    @Test
    void save_Role_OK() {
        Role role = roleDao.save(new Role(Role.RoleName.USER));
        Assertions.assertEquals(1L, role.getId());
        Assertions.assertEquals("USER", role.getRoleName().toString());
    }

    @Test
    void save_Null_Exception() {
        //DataProcessingException("Can't create entity: " + entity, e);
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.save(null),
                "Can't create entity: null");
    }

    @Test
    void getRoleByName_OK() {
        Role role = roleDao.save(new Role(Role.RoleName.USER));
        Optional<Role> actual = roleDao.getRoleByName("USER");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(true, actual.isPresent());
        Assertions.assertEquals("USER", actual.get().getRoleName().name());
    }

    @Test
    void getRoleByName_Exception() {
        Role role = roleDao.save(new Role(Role.RoleName.USER));
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("RESU"),
                "Couldn't get role by role name: RESU");
    }
}

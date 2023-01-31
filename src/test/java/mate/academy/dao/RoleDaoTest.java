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

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);

        Role actual = roleDao.save(role);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);

        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName("USER");

        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(role.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByNotExistedName_notOk() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);

        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName("ADMIN");

        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void getRoleByNotValidName_notOk() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);

        roleDao.save(role);
        try {
            roleDao.getRoleByName("USEROK");
        } catch (DataProcessingException e) {
            return;
        }
        Assertions.fail("Excepted to receive DataProcessingException");

    }
}

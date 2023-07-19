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
        return new Class[] {Role.class};
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName("USER");
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(role.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_NotOk() {
        DataProcessingException e = Assertions.assertThrows(DataProcessingException.class, () ->
                roleDao.getRoleByName("user"));
        Assertions.assertEquals("Couldn't get role by role name: user", e.getMessage());
    }
}

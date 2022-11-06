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
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void save_nullValue_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName("USER");
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(role.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_invalidValue_notOk() {
        String roleInvalid = "aDMIN";
        DataProcessingException e = Assertions.assertThrows(DataProcessingException.class, () ->
                roleDao.getRoleByName(roleInvalid));
        Assertions.assertEquals("Couldn't get role by role name: " + roleInvalid, e.getMessage());
    }
}

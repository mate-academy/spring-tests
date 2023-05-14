package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setup() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void save_notOk() {
        RoleDao roleDao1 = new RoleDaoImpl(null);
        Assertions.assertThrows(DataProcessingException.class, () ->
                roleDao1.save(role));
    }

    @Test
    void getRoleByName_ok() {
        Role expected = roleDao.save(role);
        Role actual = roleDao.getRoleByName(Role.RoleName.USER.name()).get();
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void getRoleByName_invalidSession_notOk() {
        RoleDao roleDao1 = new RoleDaoImpl(null);
        Role expected = roleDao.save(role);
        Assertions.assertThrows(DataProcessingException.class, () ->
                roleDao1.getRoleByName(expected.getRoleName().name()));
    }
}

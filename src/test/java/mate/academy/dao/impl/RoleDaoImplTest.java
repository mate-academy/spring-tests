package mate.academy.dao.impl;

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

    @Test
    public void save_ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Assertions.assertEquals(1L, role.getId());
    }

    @Test
    public void save_null_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.save(null));
    }

    @Test
    public void getRoleByName_ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Assertions.assertEquals("USER",
                roleDao.getRoleByName(Role.RoleName.USER.name()).get().getRoleName().name());
    }

    @Test
    void getRoleByName_wrongName_notOk() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Assertions.assertThrows(DataProcessingException.class, () ->
                roleDao.getRoleByName("wrongName").get());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}

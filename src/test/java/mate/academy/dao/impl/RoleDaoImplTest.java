package mate.academy.dao.impl;

import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDaoImpl roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Assertions.assertNotNull(roleDao.save(role));
    }

    @Test
    void save_NotOk_Throw_Exception() {
        Role role = null;
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.save(role));
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);
        Assertions.assertEquals(role.getRoleName(),
                roleDao.getRoleByName("USER").get().getRoleName());
    }

    @Test
    void getRoleByName_NotOk_Throw_Exception() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("admin"));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}

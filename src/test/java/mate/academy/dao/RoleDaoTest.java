package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private Role actual;
    private Role userRole;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        actual = roleDao.save(userRole);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @Test
    void roleDao_save_Ok() {
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void roleDao_RoleByName_Ok() {
        Assertions.assertEquals(Role.RoleName.USER, actual.getRoleName());
    }

    @Test
    void roleDao_RoleByFakeName_notOk() {
        Assertions.assertThrows(DataProcessingException.class, ()
                -> roleDao.getRoleByName("Wrong"));
    }

    @Test
    void roleDao_RoleByNotUniqueName_notOk() {
        roleDao.save(userRole);
        Assertions.assertThrows(DataProcessingException.class, ()
                -> roleDao.getRoleByName(userRole.getRoleName().name()));
    }
}

package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Assertions.assertEquals(1L, roleDao.save(new Role(Role.RoleName.ADMIN)).getId());
        Assertions.assertEquals(2L, roleDao.save(new Role(Role.RoleName.USER)).getId());
    }

    @Test
    void save_NullRole_Ok() {
        Assertions.assertThrows(Exception.class, () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_Ok() {
        final Role admin = new Role(Role.RoleName.ADMIN);
        final Role user = new Role(Role.RoleName.USER);
        roleDao.save(new Role(Role.RoleName.ADMIN));
        roleDao.save(new Role(Role.RoleName.USER));
        Assertions.assertEquals(Role.RoleName.ADMIN,
                roleDao.getRoleByName(Role.RoleName.ADMIN.name()).get().getRoleName());
        Assertions.assertTrue(user.getClass()
                .equals(roleDao.getRoleByName(Role.RoleName.USER.name()).get().getClass()));
    }

    @Test
    void getRoleByName_NullRole_NotOk() {
        Assertions.assertThrows(Exception.class, () -> roleDao.getRoleByName(null));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] { Role.class };
    }
}

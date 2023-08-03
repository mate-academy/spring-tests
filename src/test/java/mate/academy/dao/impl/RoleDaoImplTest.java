package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractDaoTest {
    private static final long BOB_ID = 1L;
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(Role.RoleName.USER, actual.getRoleName());
        Assertions.assertEquals(BOB_ID, actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName("USER");

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(BOB_ID, actual.get().getId());
        Assertions.assertEquals(Role.RoleName.USER, actual.get().getRoleName());
    }

    @Test
    void getRoleByName_roleNotExist_notOk() {
        Optional<Role> actual = roleDao.getRoleByName("ADMIN");
        Assertions.assertFalse(actual.isPresent());
    }
}

package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.ADMIN);
        Role actual = roleDao.save(role);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(Role.RoleName.ADMIN, actual.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.ADMIN);
        roleDao.save(role);
        Role actual = roleDao.getRoleByName("ADMIN").get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(Role.RoleName.ADMIN, actual.getRoleName());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}

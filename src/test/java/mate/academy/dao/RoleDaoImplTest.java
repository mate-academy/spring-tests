package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role = new Role();

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role.setRoleName(Role.RoleName.ADMIN);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(Role.RoleName.ADMIN, actual.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Role actual = roleDao.getRoleByName("ADMIN").get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(Role.RoleName.ADMIN, actual.getRoleName());
    }

    @Test
    void getRoleByName_WrongName_NotOk() {
        roleDao.save(role);
        Throwable exception = Assertions.assertThrows(DataProcessingException.class,
                () -> {
                roleDao.getRoleByName("PEASANT").get(); },
                "Couldn't get role by role name: PEASANT");
        Assertions.assertEquals("Couldn't get role by role name: PEASANT",
                exception.getLocalizedMessage());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}

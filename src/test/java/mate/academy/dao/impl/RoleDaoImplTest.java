package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractDaoTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class, User.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);
    }

    @Test
    void getRoleByName_Ok() {
        Role actualRole = roleDao.getRoleByName(Role.RoleName.USER.name()).get();
        Assertions.assertNotNull(actualRole);
        Assertions.assertEquals(1L, actualRole.getId());
    }

    @Test
    void getRoleByName_NoSuchElementException() {
        Exception exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> {
                    roleDao.getRoleByName(Role.RoleName.ADMIN.name()).get();
                }, "NoSuchElementException was expected");
    }
}

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
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(Role.RoleName.USER, actual.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_DataProcessingException() {
        roleDao = new RoleDaoImpl(null);
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(Role.RoleName.USER.name()));
    }
}

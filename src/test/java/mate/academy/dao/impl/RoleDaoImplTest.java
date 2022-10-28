package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
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
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    public void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    public void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(Role.RoleName.USER, actual.get().getRoleName());
    }

    @Test
    public void getRoleByName_NotOk() {
        DataProcessingException exception = Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("MODERATOR"));
        Assertions.assertEquals("Couldn't get role by role name: MODERATOR",
                exception.getMessage());
    }
}

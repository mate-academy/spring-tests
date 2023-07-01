package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleDaoImplTest extends AbstractTest {
    private static final String VALID_ROLE_NAME = "USER";
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
    }

    @Test
    void save_ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(VALID_ROLE_NAME);
        if (actual.isPresent()) {
            Assertions.assertEquals(1L, actual.get().getId());
            return;
        }
        Assertions.fail();
    }

    @Test
    void getRoleByName_notOk() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName("ADMIN");
        Assertions.assertFalse(actual.isPresent());
    }
}

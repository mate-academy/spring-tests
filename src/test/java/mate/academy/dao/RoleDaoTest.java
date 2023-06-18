package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final Role.RoleName USER_ROLE_NAME = Role.RoleName.USER;
    private static final String USER_ROLE_STRING = "USER";
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(USER_ROLE_NAME);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @Test
    void saveRole_Ok() {
        Role actualRole = roleDao.save(role);
        Assertions.assertEquals(actualRole, role);
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> optionalRole = roleDao.getRoleByName(USER_ROLE_STRING);
        Assertions.assertTrue(optionalRole.isPresent());
        Role actualRole = optionalRole.get();
        Assertions.assertEquals(actualRole.getRoleName(), role.getRoleName());
    }
}

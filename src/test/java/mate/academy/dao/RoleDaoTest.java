package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class, User.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role user = new Role(RoleName.USER);
        Role actual = roleDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(RoleName.USER, actual.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        RoleName userRoleName = RoleName.USER;
        Role userRole = new Role(userRoleName);
        userRole = roleDao.save(userRole);
        Optional<Role> actualOptional = roleDao.getRoleByName(userRoleName.name());
        Assertions.assertNotNull(actualOptional.get());
        Assertions.assertEquals(userRole.getRoleName(), actualOptional.get().getRoleName());
    }
}

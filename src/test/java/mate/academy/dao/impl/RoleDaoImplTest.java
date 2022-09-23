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
    private Role adminRole = new Role(Role.RoleName.ADMIN);
    private Role userRole = new Role(Role.RoleName.USER);

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role admin = roleDao.save(adminRole);
        Role user = roleDao.save(userRole);
        Assertions.assertNotNull(admin);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1L, admin.getId());
        Assertions.assertEquals(2L, user.getId());
        Assertions.assertEquals(adminRole.getRoleName(), admin.getRoleName());
        Assertions.assertEquals(userRole.getRoleName(), user.getRoleName());
    }

    @Test
    void getRoleByName_existRole_Ok() {
        roleDao.save(adminRole);
        String existRole = adminRole.getRoleName().name();
        Optional<Role> actual = roleDao.getRoleByName(existRole);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(adminRole.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_notExistRole_Ok() {
        roleDao.save(adminRole);
        roleDao.save(userRole);
        String notExistRole = "INCOGNITO";
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(notExistRole),
                "Expected to receive DataProcessingException");
    }
}

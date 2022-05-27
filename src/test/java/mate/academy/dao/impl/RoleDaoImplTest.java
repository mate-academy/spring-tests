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
    private Role testRole_1;
    private Role testRole_2;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        testRole_1 = new Role(Role.RoleName.ADMIN);
        testRole_2 = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actual_1 = roleDao.save(testRole_1);
        Role actual_2 = roleDao.save(testRole_2);
        Assertions.assertNotNull(actual_1);
        Assertions.assertNotNull(actual_2);
        Assertions.assertEquals(1L, actual_1.getId());
        Assertions.assertEquals(2L, actual_2.getId());
        Assertions.assertEquals(testRole_1.getRoleName(), actual_1.getRoleName());
        Assertions.assertEquals(testRole_2.getRoleName(), actual_2.getRoleName());
    }

    @Test
    void getRoleByName_existRole_Ok() {
        roleDao.save(testRole_1);
        String existRole = testRole_1.getRoleName().name();
        Optional<Role> actual = roleDao.getRoleByName(existRole);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(testRole_1.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_notExistRole_Ok() {
        roleDao.save(testRole_1);
        roleDao.save(testRole_2);
        String notExistRole = "MODERATOR";
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(notExistRole),
                "Expected to receive DataProcessingException "
                        + "while get role by non-exist roleName.\n");
    }
}

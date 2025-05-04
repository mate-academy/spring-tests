package mate.academy.dao;

import java.util.NoSuchElementException;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static RoleDao roleDao;
    private static Role roleUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeAll
    static void beforeAll() {
        roleUser = new Role(Role.RoleName.USER);
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role savedRole = roleDao.save(roleUser);
        Assertions.assertNotNull(savedRole);
        Assertions.assertEquals(1L, savedRole.getId());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(roleUser);
        Role roleFromDb = roleDao.getRoleByName(roleUser.getRoleName().name()).get();
        Assertions.assertNotNull(roleFromDb);
        Assertions.assertEquals(roleUser.getRoleName(), roleFromDb.getRoleName());
    }

    @Test
    void getRoleByName_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleDao.getRoleByName(roleUser.getRoleName().name()).get());
    }
}

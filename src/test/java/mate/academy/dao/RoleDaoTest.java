package mate.academy.dao;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private static final Role ROLE_ADMIN = new Role(Role.RoleName.ADMIN);
    private static RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(ROLE);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getRoleName(), ROLE.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(ROLE);
        Optional<Role> actual = roleDao.getRoleByName(ROLE.getRoleName().name());

        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(ROLE.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_NotOk() {
        roleDao.save(ROLE);
        Optional<Role> actual = roleDao.getRoleByName(ROLE_ADMIN.getRoleName().name());

        try {
            actual.get();
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}


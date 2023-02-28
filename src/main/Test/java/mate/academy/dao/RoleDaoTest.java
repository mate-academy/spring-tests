package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    public static final String ROLE_USER = "USER";
    public static final String INVALID_ROLE = "GUEST";
    private RoleDao roleDao;
    private Role roleUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleUser = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(roleUser);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(roleUser);
        Optional<Role> actual = roleDao.getRoleByName(ROLE_USER);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(Role.RoleName.USER, actual.get().getRoleName());
    }

    @Test
    void getRoleByName_roleNameIsNotExsist_notOk() {
        roleDao.save(roleUser);
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(INVALID_ROLE));
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null));
    }
}

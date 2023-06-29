package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private static final String FAKE_ROLE = "FAKE";
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
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(FAKE_ROLE));
    }

    @Test
    void getRoleByName_RoleNameIsNull_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}

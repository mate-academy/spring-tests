package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final String ROLE = "USER";
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ROLE, actual.getRoleName().name());
    }

    @Test
    void save_nullRole_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.save(null),
                "Expected DataProcessingException");
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actualRole = roleDao.getRoleByName(ROLE);
        Assertions.assertTrue(actualRole.isPresent());
        Role actual = actualRole.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_nullRoleName_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null),
                "Expected DataProcessingException");
    }
}

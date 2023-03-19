package mate.academy.dao;

import java.util.NoSuchElementException;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private Role userRole;

    private Role expectedRole;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role(Role.RoleName.USER);
        expectedRole = roleDao.save(userRole);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @Test
    void save_Ok() {
        Assertions.assertEquals(userRole.getId(), 1L);
    }

    @Test
    void getRoleByName_Ok() {
        Role actualRole = roleDao.getRoleByName(userRole.getRoleName().name()).get();
        Assertions.assertEquals(actualRole.getId(), 1L);
        Assertions.assertEquals(expectedRole.getRoleName(), actualRole.getRoleName());
    }

    @Test
    void getRoleByName_RoleIsNull_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null).get());
    }

    @Test
    void getRoleByName_RoleNotCorrect_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleDao.getRoleByName("ADMIN").get());
    }
}

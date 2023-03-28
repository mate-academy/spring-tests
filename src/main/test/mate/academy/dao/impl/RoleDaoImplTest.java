package mate.academy.dao.impl;

import java.util.NoSuchElementException;

import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractDaoTest {
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
    void save_ok() {
        Assertions.assertEquals(userRole.getId(), 1L);
    }

    @Test
    void getRoleByName_ok() {
        Role actualRole = roleDao.getRoleByName(userRole.getRoleName().name()).get();
        Assertions.assertEquals(actualRole.getId(), 1L);
        Assertions.assertEquals(expectedRole.getRoleName(), actualRole.getRoleName());
    }

    @Test
    void getRoleByName_nullRole_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null).get());
    }

    @Test
    void getRoleByName_wrongRole_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleDao.getRoleByName("ADMIN").get());
    }
}

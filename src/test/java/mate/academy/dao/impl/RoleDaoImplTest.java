package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role userRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void beforeAll() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role(Role.RoleName.USER);
    }

    @Test
    void save_ok() {
        Role actual = roleDao.save(userRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_ok() {
        String expected = "USER";
        roleDao.save(userRole);
        Role actual = roleDao.getRoleByName(expected).get();
        Assertions.assertEquals(expected, actual.getRoleName().name());
    }

    @Test
    void getRoleByName_emptyValue_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () ->
                roleDao.getRoleByName(""));
    }

    @Test
    void getRoleByName_nullValue_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () ->
                roleDao.getRoleByName(null));
    }
}

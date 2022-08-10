package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final Long ID_1 = 1L;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(new Role(Role.RoleName.USER).getRoleName());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @Test
    void save_validRole_ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID_1, actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void save_nullData_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_validRoleName_ok() {
        roleDao.save(role);
        Optional<Role> actualOptional = roleDao.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actualOptional);
        Role actual = actualOptional.get();
        Assertions.assertEquals(ID_1, actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_noExistRoleName_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("Non exist role name"));
    }

    @Test
    void getRoleByName_nullRoleName_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null));
    }
}

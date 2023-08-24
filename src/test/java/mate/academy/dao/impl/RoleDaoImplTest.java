package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractDaoTest {

    private static final String INCORRECT_ROLE = "USERNOTOK";
    private RoleDao roleDao;
    private Role commonRole;

    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        commonRole = new Role();
        commonRole.setRoleName(Role.RoleName.USER);
        roleDao.save(commonRole);
    }

    @Test
    void save_ok() {
        commonRole.setRoleName(Role.RoleName.USER);
        Role actual = roleDao.save(commonRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(2L, actual.getId());
        Assertions.assertEquals(commonRole.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_ok() {
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(commonRole.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_ByNotExistedName_notOk() {
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.ADMIN.name());
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void getRoleByName_InvalidName_notOk() {
        try {
            roleDao.getRoleByName(INCORRECT_ROLE);
        } catch (DataProcessingException e) {
            return;
        }
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(INCORRECT_ROLE));
    }
}

package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final Long ID = 1L;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @Test
    void save_ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID, actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(ID, actual.get().getId());
        Assertions.assertEquals(role.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_nullName_notOK() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null));
    }

    @Test
    void getRoleByName_notExistingName_notOK() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("NOT-A-ROLE"));
    }
}

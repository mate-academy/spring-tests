package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void getRoleByName_Ok() {
        Role saved = roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(saved.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(saved.getRoleName().name(),
                actual.get().getRoleName().name());
    }

    @Test
    void getRoleByName_roleNotFound_ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.ADMIN.name());
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void getRoleByName_roleNameIsNull_notOk() {
        Assertions.assertThrows(
                DataProcessingException.class,
                () -> roleDao.getRoleByName(null)
        );
    }

    @Test
    void save_roleIsNull_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.save(null));
    }
}

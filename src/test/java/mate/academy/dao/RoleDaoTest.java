package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    public static final Long ROLE_ID = 1L;
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
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
        Assertions.assertEquals(ROLE_ID, actual.getId());
        Assertions.assertEquals(Role.RoleName.USER, actual.getRoleName());
    }

    @Test
    void save_RoleIsNull_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> optionalRole = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertTrue(optionalRole.isPresent());
        Assertions.assertEquals(role.getRoleName(), optionalRole.get().getRoleName());
        Assertions.assertEquals(role.getId(), optionalRole.get().getId());
    }

    @Test
    void getRoleByName_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null).get());
    }
}

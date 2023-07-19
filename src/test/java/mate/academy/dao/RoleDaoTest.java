package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleDaoTest extends AbstractTest {
    private static final String VALID_ROLE = "ADMIN";
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.ADMIN);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao = Mockito.mock(RoleDao.class);
        Mockito.when(roleDao.getRoleByName(Role.RoleName.ADMIN.name()))
                .thenReturn(Optional.of(role));
        Optional<Role> actual = roleDao.getRoleByName(VALID_ROLE);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(role.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_notOk() {
        roleDao = Mockito.mock(RoleDao.class);
        Mockito.when(roleDao.getRoleByName(VALID_ROLE)).thenReturn(Optional.empty());
        Optional<Role> role = roleDao.getRoleByName(VALID_ROLE);
        Assertions.assertFalse(role.isPresent(), "Expected the role to be empty");
    }
}

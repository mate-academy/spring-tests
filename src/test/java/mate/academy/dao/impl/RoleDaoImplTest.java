package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;
    private Role actual;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
        actual = roleDao.save(role);
    }

    @Test
    void save_ok() {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_ok() {
        Optional<Role> optionalRole = roleDao.getRoleByName("USER");
        Assertions.assertTrue(optionalRole.isPresent());
        Role.RoleName actualName = optionalRole.get().getRoleName();
        Assertions.assertEquals(role.getRoleName(), actualName);
    }

    @Test
    void getRoleByName_notFound_notOk() {
        try {
            roleDao.getRoleByName("MANAGER");
        } catch (Exception e) {
            Assertions.assertThrows(DataProcessingException.class,
                    () -> roleDao.getRoleByName("MANAGER"));
        }
    }
}

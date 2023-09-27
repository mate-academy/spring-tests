package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final String INVALID_ROLE_NAME = "Invalid";
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    public void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
    }

    @Test
    public void saveUser_Ok() {
        Role savedRole = roleDao.save(role);
        Assertions.assertNotNull(savedRole);
        Assertions.assertEquals(role, savedRole);
    }

    @Test
    public void getRoleByName_ExistentRoleName_Ok() {
        roleDao.save(role);
        Optional<Role> retrievedRole = roleDao
                .getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(retrievedRole);
        Assertions.assertTrue(retrievedRole.isPresent());
        Assertions.assertEquals(role.getRoleName().name(),
                retrievedRole.get().getRoleName().name());
    }

    @Test
    public void getRoleByName_NotExistentRoleName_NotOk() {
        assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(INVALID_ROLE_NAME),
                "DataProcessingException to be thrown, but nothing was thrown");
    }
}

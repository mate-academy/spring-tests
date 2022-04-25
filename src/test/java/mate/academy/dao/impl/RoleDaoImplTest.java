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
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class, Role.RoleName.class};
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
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void add_duplicatedRole_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.save(role),
                "Expected DataProcessingException when saving Role which already exist in DB");
    }

    @Test
    void getRoleByName_Ok() {
        Role savedRole = roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedRole.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_nonExistentRole_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.ADMIN.name());
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }
}

package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDaoImpl roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        Role userRole = new Role(Role.RoleName.USER);
        roleDao.save(userRole);
        Assertions.assertEquals(1L, userRole.getId());

        Role adminRole = new Role(Role.RoleName.ADMIN);
        roleDao.save(adminRole);
        Assertions.assertEquals(2L, adminRole.getId());
    }

    @Test
    void findById_validId_shouldFind() {
        Role userRole = new Role(Role.RoleName.USER);
        roleDao.save(userRole);
        Optional<Role> optionalActual = roleDao.findById(userRole.getId());
        Assertions.assertTrue(optionalActual.isPresent());
        Role actual = optionalActual.get();
        Assertions.assertEquals(userRole, actual);
    }

    @Test
    void findById_notExistingId_shouldReturnEmptyOptional() {
        Optional<Role> actual = roleDao.findById(5L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void getRoleByName_validRoleName_shouldFind() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Optional<Role> optionalActual = roleDao.getRoleByName(role.getRoleName().name());
        Assertions.assertTrue(optionalActual.isPresent());
        Role actual = optionalActual.get();
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_unValidRoleName_shouldReturnEmptyOptional() {
        String unValidRoleName = "Unvalid roleName";
        try {
            roleDao.getRoleByName(unValidRoleName);
        } catch (Exception e) {
            Assertions.assertEquals("Couldn't get role by role name: " + unValidRoleName,
                    e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}

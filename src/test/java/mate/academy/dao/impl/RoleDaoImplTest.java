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
    private Role userRole;
    private String nonExistentRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class, Role.RoleName.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role(Role.RoleName.USER);
        nonExistentRole = "STUDENT";
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(userRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userRole, actual);
    }

    @Test
    void getRoleByName_Ok() {
        Role saveRole = roleDao.save(userRole);
        Optional<Role> actualOptional = roleDao.getRoleByName(userRole.getRoleName().name());
        Assertions.assertNotNull(actualOptional.get());
        Assertions.assertEquals(saveRole.getRoleName(), actualOptional.get().getRoleName());
    }

    @Test
    void getRoleByName_nonExistentRole_NotOk() {
        try {
            roleDao.getRoleByName(nonExistentRole);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: "
                    + nonExistentRole, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException while"
                + " getting non existent role");
    }
}

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
    Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName("USER");
        Assertions.assertTrue(actual.isPresent(), "Role USER is present in DB" );
        Assertions.assertEquals("USER", actual.get().getRoleName().name());
    }

    @Test
    void getRoleByName_DataProcessing() {
        roleDao.save(role);
        try {
            roleDao.getRoleByName("HACKER");
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: "
                    + "HACKER", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }
}

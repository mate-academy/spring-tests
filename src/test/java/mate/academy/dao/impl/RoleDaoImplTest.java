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

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_correctRole_isOk() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_correctName_isOk() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Role saveDRole = roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(String.valueOf(saveDRole.getRoleName()));
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(role.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_notCorrectRoleName_notOk() {
        String roleName = "CHILD";
        try {
            roleDao.getRoleByName(roleName);
        } catch (DataProcessingException e) {
            Assertions.assertEquals(e.getMessage(), "Couldn't get role by role name: "
                    + roleName);
            return;
        }
        Assertions.fail();
    }
}

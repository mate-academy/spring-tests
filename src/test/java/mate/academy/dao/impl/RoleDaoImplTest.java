package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final String ROLE = "STUDENT";
    private RoleDao roleDao;
    private Role userRole;
    private String nonExistentRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class, Role.RoleName.class};
    }

    @BeforeEach
   public void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role(Role.RoleName.USER);
        nonExistentRole = ROLE;
    }

    @Test
    public void save_Ok() {
        Role actual = roleDao.save(userRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userRole.getRoleName(), actual.getRoleName());
    }

    @Test
    public void getRoleByName_ok() {
        Role saveRole = roleDao.save(userRole);
        Optional<Role> actualOptional = roleDao.getRoleByName(userRole.getRoleName().name());
        Assertions.assertNotNull(actualOptional.get());
        Assertions.assertEquals(saveRole.getRoleName(), actualOptional.get().getRoleName());
    }

    @Test
    public void getRoleByName_nonExistentRole_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(nonExistentRole));
    }
}

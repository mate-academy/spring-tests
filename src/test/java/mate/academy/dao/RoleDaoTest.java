package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private final Role role = new Role(Role.RoleName.ADMIN);
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        Role admin = roleDao.save(role);
        Assertions.assertNotNull(admin);
        Assertions.assertEquals(1L, admin.getId());
        Assertions.assertEquals(role.getRoleName(), admin.getRoleName());
    }

    @Test
    void getRoleByName_ok() {
        Role admin = roleDao.save(role);
        Optional<Role> roleByName = roleDao.getRoleByName(admin.getRoleName().toString());
        Assertions.assertNotNull(roleByName);
        Assertions.assertEquals(admin.getRoleName(), roleByName.get().getRoleName());
    }

    @Test
    void getRoleByName_incorrectRoleName_notOk() {
       Assertions.assertThrows(DataProcessingException.class,() -> roleDao
               .getRoleByName("CUSTOMER"), "Planning to get a DataProcessingException");
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }
}

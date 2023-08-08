package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.exception.DataProcessingException;
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
    void save_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);
        Optional<Role> byName = roleDao.getRoleByName("USER");
        Assertions.assertEquals("USER", byName.get().getRoleName().name());
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);
        Assertions.assertEquals(role.getRoleName(),
                roleDao.getRoleByName("USER").get().getRoleName());
    }

    @Test
    void getRoleByName_Not_Existing_Role_Not_Ok() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("admin"));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}

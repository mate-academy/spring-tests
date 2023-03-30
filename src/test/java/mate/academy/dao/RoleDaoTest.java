package mate.academy.dao;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private Role adminRole;
    private Role userRole;
    
    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
    
    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role(Role.RoleName.USER);
        adminRole = new Role(Role.RoleName.ADMIN);
        roleDao.save(userRole);
    }
    
    @Test
    void save_Ok() {
        Role actual = roleDao.save(adminRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(2L, actual.getId());
    }
    
    @Test
    void getRoleByName_Ok() {
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(Role.RoleName.USER, actual.get().getRoleName());
    }
    
    @Test
    void getRoleByName_roleNotFound_notOk() {
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.ADMIN.name());
        Assertions.assertThrows(NoSuchElementException.class, actual::get);
    }
}

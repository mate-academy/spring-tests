package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest{
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        Role roleUser = new Role(Role.RoleName.USER);
        Role save = roleDao.save(roleUser);
        Assertions.assertNotNull(save);
        Assertions.assertEquals(1L,save.getId());
    }

    @Test
    void getRoleByName_ok() {
        Role roleUser = new Role(Role.RoleName.USER);
        Role save = roleDao.save(roleUser);
        Assertions.assertNotNull(save);
        Optional<Role> roleByName = roleDao.getRoleByName(roleUser.getRoleName().name());
        Role role = roleByName.orElseThrow();
        Assertions.assertEquals(save.getRoleName(), role.getRoleName());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }
}
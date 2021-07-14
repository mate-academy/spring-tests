package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
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
    public void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void saveValidEntity_ok(){
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Role roleFromDB = roleDao.save(role);
        Assertions.assertEquals(1L, roleFromDB.getId());
    }

    @Test
    void getRoleByName_validRole_ok(){
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Role roleFromDB = roleDao.save(role);
        Assertions.assertEquals(roleFromDB.toString(), roleDao.getRoleByName("USER").get().toString());
    }

    @Test
    void getRoleByName_invalidRole_ok(){
        Assertions.assertEquals(Optional.ofNullable(null), roleDao.getRoleByName("ADMIN"));
    }
}
package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role roleUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleUser = new Role(Role.RoleName.USER);
    }

    @Test
    void save_ok() {
        Role role = roleDao.save(roleUser);
        Assertions.assertEquals(role.getId(),1L);
    }

    @Test
    void getRoleByName_Ok() {
        Role role = roleDao.save(roleUser);
        Role roleFromDb = roleDao.getRoleByName(roleUser.getRoleName().name()).get();
        Assertions.assertEquals(roleFromDb.getId(),1L);
        Assertions.assertEquals(roleFromDb.getRoleName(),role.getRoleName());
    }

    @Test
    void getRoleByName_WrongRole_NotOk() {
        roleDao.save(roleUser);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleDao.getRoleByName("ADMIN").get());
    }

    @Test
    void getRoleByName_Null_NotOk() {
        roleDao.save(roleUser);
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null).get());
    }
}

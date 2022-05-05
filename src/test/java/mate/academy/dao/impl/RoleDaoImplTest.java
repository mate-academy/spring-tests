package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.util.UserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role role = roleDao.save(UserTestUtil.getUserRole());
        Assertions.assertEquals(1L , role.getId());
        Assertions.assertEquals(Role.RoleName.USER, role.getRoleName());
    }

    @Test
    void saveNull_NotOk() {
        try {
            Role role = roleDao.save(null);
            Assertions.fail("it should thrown exception");
        } catch (DataProcessingException e) {
            Assertions.assertTrue(e.getMessage().contains("Can't create entity: "));
        }
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(UserTestUtil.getUserRole());
        Optional<Role> actual = roleDao
                .getRoleByName(Role.RoleName.USER.name());
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(Role.RoleName.USER, actual.get().getRoleName());
        Assertions.assertEquals(1L, actual.get().getId());

    }

    @Test
    void getRoleByNameNoInDatabase_NotOk() {
            Optional<Role> noPresentedRole = roleDao
                    .getRoleByName(Role.RoleName.ADMIN.name());
            Assertions.assertTrue(noPresentedRole.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class, User.class};
    }

}

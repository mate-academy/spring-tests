package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final Long USER_ROLE_ID = 1L;
    private static final String MEMBER_ROLE = "MEMBER";
    private Role admin;
    private Role user;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        admin = new Role(Role.RoleName.ADMIN);
        user = new Role(Role.RoleName.USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @Test
    void save_saveRole_ok() {
        Role userRole = roleDao.save(user);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(USER_ROLE_ID, userRole.getId());

    }

    @Test
    void getRoleByName_twoRolesSavedGetRight_ok() {
        roleDao.save(new Role(Role.RoleName.ADMIN));
        roleDao.save(new Role(Role.RoleName.USER));
        Optional<Role> actualAdmin = roleDao.getRoleByName(Role.RoleName.ADMIN.name());
        Optional<Role> actualUser = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertEquals(admin.getRoleName(), actualAdmin.get().getRoleName());
        Assertions.assertEquals(user.getRoleName(), actualUser.get().getRoleName());
    }

    @Test
    void getRoleByName_NotValidName_notOk() {
        roleDao.save(new Role(Role.RoleName.ADMIN));
        roleDao.save(new Role(Role.RoleName.USER));
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(MEMBER_ROLE));
    }
}

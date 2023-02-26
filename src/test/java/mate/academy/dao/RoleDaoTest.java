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
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";
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
    void saveRole_Ok() {
        Role roleUser = new Role();
        roleDao.save(roleUser);
        Assertions.assertNotNull(roleUser);
        Assertions.assertEquals(USER_ROLE_ID, roleUser.getId());

    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(new Role(Role.RoleName.ADMIN));
        roleDao.save(new Role(Role.RoleName.USER));
        Optional<Role> actualAdmin = roleDao.getRoleByName(ADMIN_ROLE);
        Optional<Role> actualUser = roleDao.getRoleByName(USER_ROLE);
        Assertions.assertEquals(admin.getRoleName(), actualAdmin.get().getRoleName());
        Assertions.assertEquals(user.getRoleName(), actualUser.get().getRoleName());
    }

    @Test
    void getRoleByNotValidName_NotOk() {
        roleDao.save(new Role(Role.RoleName.ADMIN));
        roleDao.save(new Role(Role.RoleName.USER));
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(MEMBER_ROLE));
    }
}

package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;


class RoleDaoTest extends AbstractTest {
    private final static Long USER_ROLE_ID = 1L;
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
        Optional<Role> actualAdmin = roleDao.getRoleByName("ADMIN");
        Optional<Role> actualUser = roleDao.getRoleByName("USER");
        Assertions.assertEquals(admin.getRoleName(), actualAdmin.get().getRoleName());
        Assertions.assertEquals(user.getRoleName(), actualUser.get().getRoleName());

    }
}
package mate.academy.dao;

import static mate.academy.model.Role.RoleName.ADMIN;
import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private Role roleAdmin;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleAdmin = new Role();
        roleAdmin.setRoleName(ADMIN);
    }

    @Test
    @Order(1)
    void save_role_ok() {
        Role actualRole = roleDao.save(roleAdmin);
        Assertions.assertNotNull(actualRole);
        Assertions.assertEquals(1L, actualRole.getId());

    }

    @Test
    @Order(2)
    void getRoleByName_roleAdmin_ok() {
        roleDao.save(roleAdmin);
        Optional<Role> optionalRole = roleDao.getRoleByName(ADMIN.name());
        assertTrue(optionalRole.isPresent());
        assertEquals(ADMIN.name(), optionalRole.get().getRoleName().name());
    }

    @Test
    @Order(3)
    void getRoleByName_roleUser_notOk() {
        roleDao.save(roleAdmin);
        Optional<Role> optionalRole = roleDao.getRoleByName(USER.name());
        assertFalse(optionalRole.isPresent());
    }
}

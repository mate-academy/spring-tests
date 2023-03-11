package mate.academy.dao;

import static mate.academy.model.Role.RoleName.ADMIN;
import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
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
    void save_roleAdmin_ok() {
        Role actualRole = roleDao.save(roleAdmin);
        assertNotNull(actualRole,
                "Method should return saved role '%s'"
                        .formatted(roleAdmin.getRoleName()));
        assertEquals(1L, actualRole.getId());
    }

    @Test
    @Order(2)
    void getRoleByName_roleAdmin_ok() {
        roleDao.save(roleAdmin);
        Optional<Role> optionalRole = roleDao.getRoleByName(roleAdmin.getRoleName().name());
        assertTrue(optionalRole.isPresent());
        assertEquals(roleAdmin.getRoleName(), optionalRole.get().getRoleName(),
                "Method should contain optional role '%s' for role name '%s'"
                        .formatted(roleAdmin.getRoleName(), roleAdmin.getRoleName().name()));
    }

    @Test
    @Order(3)
    void getRoleByName_roleUserMissingInDb_notOk() {
        roleDao.save(roleAdmin);
        Optional<Role> optionalRole = roleDao.getRoleByName(USER.name());
        assertFalse(optionalRole.isPresent(),
                "Method should return empty optional if role is missing in DB");
    }

    @Test
    @Order(4)
    void getRoleByName_notExistingRoleName_notOk() {
        roleDao.save(roleAdmin);
        String notExistingRoleName = "NOT_EXIST";
        assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(notExistingRoleName),
                "Method should through '%s' for non existing roleName '%s'"
                        .formatted(DataProcessingException.class, notExistingRoleName));
    }
}

package mate.academy.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private AbstractDao<Role, Long> roleAbstractDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleAbstractDao = new RoleDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    private List<Role> injectRoles() {
        Role user = new Role(Role.RoleName.USER);
        Role admin = new Role(Role.RoleName.ADMIN);
        roleDao.save(user);
        roleDao.save(admin);
        return List.of(user, admin);
    }

    @Test
    void saveRole_Ok() {
        Role roleUser = new Role(Role.RoleName.USER);
        Role actual = roleDao.save(roleUser);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findAllRoles_Ok() {
        List<Role> expectedRoles = injectRoles();
        List<Role> actualRoles = roleAbstractDao.findAll();
        Assertions.assertNotNull(actualRoles);
        Assertions.assertArrayEquals(expectedRoles.stream().map(Role::toString).toArray(),
                actualRoles.stream().map(Role::toString).toArray());
    }

    @Test
    void findAllNotExistedRoles_EmptyList() {
        List<Role> expectedRoles = new ArrayList<>();
        List<Role> actualRoles = roleAbstractDao.findAll();
        Assertions.assertNotNull(actualRoles);
        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    @Test
    void findRoleById_Ok() {
        List<Role> expectedUsers = injectRoles();
        Long id = 2L;
        Optional<Role> actual = roleAbstractDao.findById(id);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expectedUsers.get(1), actual.get());
    }

    @Test
    void findRoleByNotExistedId_Ok() {
        injectRoles();
        Long id = 3L;
        Optional<Role> actual = roleAbstractDao.findById(id);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void deleteRoleById_Ok() {
        injectRoles();
        Long id = 1L;
        roleAbstractDao.delete(id);
        Optional<Role> actual = roleAbstractDao.findById(id);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void deleteRoleByNotExistedId_DataProcessingException() {
        injectRoles();
        Long id = 3L;
        try {
            roleAbstractDao.delete(id);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't delete entity by id: " + id, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void updateRole_Ok() {
        injectRoles();
        Role admin = new Role(Role.RoleName.ADMIN);
        admin.setId(1L);
        Role actual = roleAbstractDao.update(admin);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(admin, actual);
        Assertions.assertEquals(admin, roleAbstractDao.findById(1L).get());
    }

    @Test
    void getRoleByName_Ok() {
        injectRoles();
        String email = "ADMIN";
        Optional<Role> actual = roleDao.getRoleByName(email);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(2L, actual.get().getId());
    }
}

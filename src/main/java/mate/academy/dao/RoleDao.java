package mate.academy.dao;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Role;

public interface RoleDao {
    Role save(Role role);

    Optional<Role> getRoleByName(String roleName);

    List<Role> findAll();
}

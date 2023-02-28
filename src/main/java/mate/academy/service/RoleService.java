package mate.academy.service;

import java.util.List;
import mate.academy.model.Role;

public interface RoleService {
    Role save(Role role);

    Role getRoleByName(String roleName);

    List<Role> findAll();
}

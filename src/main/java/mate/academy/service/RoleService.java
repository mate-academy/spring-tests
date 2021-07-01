package mate.academy.service;

import mate.academy.model.Role;

public interface RoleService {
    Role save(Role role);

    Role getRoleByName(String roleName);
}

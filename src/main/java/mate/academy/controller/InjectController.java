package mate.academy.controller;

import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inject")
public class InjectController {
    private final RoleService roleService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public InjectController(RoleService roleService, AuthenticationService authenticationService,
                            UserService userService) {
        this.roleService = roleService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping
    public String injectData() {
        Role.RoleName adminRole = Role.RoleName.ADMIN;
        Role.RoleName userRole = Role.RoleName.USER;
        roleService.save(new Role(adminRole));
        roleService.save(new Role(userRole));
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("12345678");
        bob.setRoles(Set.of(roleService.getRoleByName(adminRole.name())));
        userService.save(bob);
        authenticationService.register("alice@i.ua", "12345678");
        return "Done!";
    }
}

package mate.academy.controller;

import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inject")
public class InjectController {
    private final RoleService roleService;

    private final AuthenticationService authenticationService;

    public InjectController(RoleService roleService,
                            AuthenticationService authenticationService) {
        this.roleService = roleService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String injectData() {
        roleService.save(new Role(Role.RoleName.ADMIN));
        roleService.save(new Role(Role.RoleName.USER));

        User bob = authenticationService.register("bob@i.com", "1234");
        bob.setRoles(Set.of(roleService.getRoleByName(Role.RoleName.USER.name())));
        User alice = authenticationService.register("alice@i.com", "5678");
        alice.setRoles(Set.of(roleService.getRoleByName(Role.RoleName.ADMIN.name())));
        User anotherUser = authenticationService.register("user@i.com", "9012");
        anotherUser.setRoles(Set.of(roleService.getRoleByName(Role.RoleName.ADMIN.name())));

        return "Done!";
    }
}

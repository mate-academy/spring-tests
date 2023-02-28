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
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public InjectController(RoleService roleService,
                            UserService userService,
                            AuthenticationService authenticationService) {
        this.roleService = roleService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String injectData() {
        roleService.save(new Role(Role.RoleName.ADMIN));
        roleService.save(new Role(Role.RoleName.USER));

        authenticationService.register("bob", "1234");
        authenticationService.register("alice", "1234");
        authenticationService.register("makar", "4321");
        Role roleAdmin = roleService.getRoleByName("ADMIN");

        User den = new User();
        den.setEmail("densh");
        den.setPassword("1234");
        den.setRoles(Set.of(roleAdmin));
        userService.save(den);

        User alex = new User();
        alex.setEmail("alexsh");
        alex.setPassword("4321");
        alex.setRoles(Set.of(roleAdmin));
        userService.save(alex);

        return "Done!";
    }
}

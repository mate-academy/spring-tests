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

    public InjectController(RoleService roleService, UserService userService,
                            AuthenticationService authenticationService) {
        this.roleService = roleService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String injectData() {
        roleService.save(new Role(Role.RoleName.ADMIN));
        roleService.save(new Role(Role.RoleName.USER));

        authenticationService.register("bob@i.ua", "12345678");
        authenticationService.register("alice@i.ua", "12345678");

        User admin = new User();
        admin.setEmail("john@i.ua");
        admin.setPassword("12345678");
        admin.setRoles(Set.of(roleService.getRoleByName("ADMIN")));
        userService.save(admin);

        return "Done!";
    }
}

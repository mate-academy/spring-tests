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

    public InjectController(RoleService roleService, UserService userService, AuthenticationService authenticationService) {
        this.roleService = roleService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String injectData() {
        roleService.save(new Role(Role.RoleName.ADMIN));
        roleService.save(new Role(Role.RoleName.USER));
        authenticationService.register("slave@gmail.com", "12345678");
        User master = new User();
        master.setEmail("master@gmail.com");
        master.setPassword("12345678");
        master.setRoles(Set.of(roleService.getRoleByName("ADMIN")));
        userService.save(master);
        return "Done!";
    }
}

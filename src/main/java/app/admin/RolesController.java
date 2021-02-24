package app.admin;

import app.entities.Role;
import app.entities.User;
import app.repositories.RoleRepository;
import app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RolesController {

    private final RoleRepository roleRepository;

    @Autowired
    public RolesController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/admin/roles")
    public String home(Model model) {
        Pageable pageable = PageRequest.of(1, 1);
        Page<Role> page = roleRepository.findAll(pageable);
        model.addAttribute("page", page);
        return "/admin/roles/index";
    }
}


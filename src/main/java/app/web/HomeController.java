package app.web;

import app.dto.FindUserDTO;
import app.email.EmailService;
import app.entities.Role;
import app.entities.User;

import java.util.*;

import app.repositories.RoleRepository;
import app.repositories.UserRepository;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public HomeController(UserRepository userRepository,
                          UserService userService,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String home(@RequestParam(name = "page", defaultValue = "1") int pageNo,
                       @RequestParam(name = "sortField", defaultValue = "name") String sortField,
                       @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                       Model model) {
        //pageNo = pageNo==null ? 1 : pageNo;
        //Page<User> page = userService.findPaginated(pageNo, 2, sortField, sortDir);
        FindUserDTO findUser = new FindUserDTO();
//        findUser.setName("Анджеліна");
//        findUser.setEmail("gmail");
        Page<User> page = userService.findUserPaginated(findUser, pageNo, 2, sortField, sortDir);
        List<User> users = page.getContent();//userRepository.findAll();
        model.addAttribute("users", users);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "index";
    }

    @GetMapping("/create")
    public String showSignUpForm(User user) {
        return "create";
    }

    @PostMapping("/create")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Long id, User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "edit";
        }
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        return "redirect:/";
    }

}

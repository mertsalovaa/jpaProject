package app.web;

import app.dto.FindUserDTO;
import app.dto.ForgotPasswordDTO;
import app.email.EmailService;
import app.entities.User;
import app.repositories.RoleRepository;
import app.repositories.UserRepository;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Random;

@Controller
public class AccountController {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountController(EmailService emailService,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        return "forgotPassword";
    }

    @PostMapping("/sendMessagePassword")
    public String sendResetPassword(@Valid ForgotPasswordDTO forgotPasswordDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "forgotPassword";
        }
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        emailService.sendSimpleMessage(forgotPasswordDTO.getEmail(),
                "Reset password !", "Your new password : " + String.format("%06d", number));
        User user = userRepository.findByEmail(forgotPasswordDTO.getEmail());
        user.setPassword(passwordEncoder.encode(String.format("%06d", number)));
        userRepository.save(user);
        return "login";
    }
}

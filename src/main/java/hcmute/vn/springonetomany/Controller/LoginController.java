package hcmute.vn.springonetomany.Controller;

import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Service.EmailService;
import hcmute.vn.springonetomany.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/login")
    private String showLoginForm() {
        return "signin_form";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "signup_form";
        }
        try {
            userService.registerDefaultUser(user);
            emailService.sendRegisterMail(user);
        } catch (Exception e) {
            if (userService.existsUserByEmail(user.getEmail())) {
                return "redirect:/register?existsEmail";
            }
        }
        return "redirect:/login?signup_success";
    }
}

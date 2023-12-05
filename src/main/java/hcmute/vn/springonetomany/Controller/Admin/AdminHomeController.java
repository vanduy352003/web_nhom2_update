package hcmute.vn.springonetomany.Controller.Admin;

import hcmute.vn.springonetomany.Custom.CustomUser;
import hcmute.vn.springonetomany.Custom.CustomUserDetails;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Service.ProductService;
import hcmute.vn.springonetomany.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    private String showAdminPage(@AuthenticationPrincipal CustomUser loggedUser, HttpSession session) {
        if (loggedUser != null) {
            User user = userService.findUserByEmail(loggedUser.getEmail());
            session.setAttribute("user", user);
        }
        return "admin/admin_page";
    }
}

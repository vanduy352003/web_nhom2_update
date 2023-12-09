package hcmute.vn.springonetomany.Controller;

import hcmute.vn.springonetomany.Custom.CustomUser;
import hcmute.vn.springonetomany.Custom.CustomUserDetails;
import hcmute.vn.springonetomany.Entities.Role;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Repository.IRoleRepository;
import hcmute.vn.springonetomany.Service.UserService;
import hcmute.vn.springonetomany.Ultis.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    UserService userService;

    @Autowired
    IRoleRepository roleRepository;

    @GetMapping("")
    public String viewUserDetail(@AuthenticationPrincipal CustomUser loggedUser, Model model) {
        String email = loggedUser.getEmail();
        User user = userService.findUserByEmail(email);
        List<Role> listRole = roleRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("roles", listRole);
        model.addAttribute("url", "/account/update");

        return "user/user_form";
    }

    @PostMapping("/update")
    public String updateUserDetail(@Valid User user,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   @RequestParam(value = "id", required = false) Long id,
                                   @RequestParam(value = "gender", required = false, defaultValue = "Nam") String gender,
                                   @RequestParam(value = "image", required = false) MultipartFile multipartFile
                                   ) throws IOException {
        if (result.hasErrors()) {
            return "user/user_form";
        }

        User existedUser = userService.getUserById(id);
        String fileName = (multipartFile != null && !multipartFile.isEmpty())
                ? StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()))
                : existedUser.getPhotos();

        user.setPhotos(fileName);
        user.setRoles(new HashSet<>(existedUser.getRoles()));
        user.setGender(gender);

        User savedUser = userService.updateUser(user);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String uploadDir = "user_photos/" + savedUser.getId();
            FileUploadUtil.deleteAllFiles(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        redirectAttributes.addFlashAttribute("message", "Cập nhật tài khoản thành công");
        return "redirect:/home";
    }
}

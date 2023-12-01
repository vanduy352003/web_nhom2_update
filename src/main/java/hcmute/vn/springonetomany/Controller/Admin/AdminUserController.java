package hcmute.vn.springonetomany.Controller.Admin;

import hcmute.vn.springonetomany.Entities.Role;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Repository.IRoleRepository;
import hcmute.vn.springonetomany.Service.UserService;
import hcmute.vn.springonetomany.Ultis.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    @Autowired
    UserService userService;

    @Autowired
    IRoleRepository roleRepository;

    @GetMapping("")
    public String showUsersPage(Model model) {
        List<User> listUser = userService.listAll();

        model.addAttribute("listUser", listUser);
        return "user/admin_users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUSer(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/new")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "user/user_form";
    }

    @GetMapping("/edit/{id}")
    public String editUser(Model model, @PathVariable("id") long id) {
        User user = userService.getUserById(id);
        List<Role> listRole = roleRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("roles", listRole);
        model.addAttribute("url", "/admin/users/save");

        return "user/user_form";
    }

    @PostMapping("/save")
    public String saveUser(@Valid User user,
                           BindingResult result,
                           @RequestParam(value = "id", required = false) Long id,
                           @RequestParam(value = "image") MultipartFile multipartFile ) throws IOException {
        if (result.hasErrors()) {
            return "user/user_form";
        }

        String fileName = id == null || (multipartFile != null && !multipartFile.isEmpty())
                ? StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()))
                : userService.getUserById(id).getPhotos();
        user.setPhotos(fileName);
        User savedUser = userService.updateUser(user);
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String uploadDir = "user_photos/" + savedUser.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        return "redirect:/admin/users";
    }
}

package hcmute.vn.springonetomany.Controller.Admin;

import hcmute.vn.springonetomany.Entities.Rating;
import hcmute.vn.springonetomany.Entities.RatingImage;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.Role;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Entities.Voucher;
import hcmute.vn.springonetomany.Repository.IRoleRepository;
import hcmute.vn.springonetomany.Service.RatingImageService;
import hcmute.vn.springonetomany.Service.RatingService;
import hcmute.vn.springonetomany.Service.UserService;
import hcmute.vn.springonetomany.Service.VoucherService;
import hcmute.vn.springonetomany.Ultis.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    RatingService ratingService;
    @Autowired
    RatingImageService ratingImageService;

    VoucherService voucherService;
    @Autowired
    IRoleRepository roleRepository;

    @GetMapping("")
    public String showUsersPage(Model model,
                                @RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "createdAt") String fieldName,
                                @RequestParam(required = false, defaultValue = "asc") String sortDir) {
//        List<User> listUser = userService.listAll();
        Page<User> listUser = userService.findPage(page, fieldName, sortDir);
        int totalPages = listUser.getTotalPages();
        long totalItems = listUser.getTotalElements();

        model.addAttribute("listUser", listUser.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        return "user/admin_users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUSer(@PathVariable("id") Long id) throws IOException {
        User user = userService.getUserById(id);
        for (Rating rating : user.getRatings()) {
    		for (RatingImage ratingImage : rating.getRatingImages()) {
        		ratingImageService.delete(ratingImage);
        	}
        	FileUploadUtil.deleteAllFiles("rating_images/" + rating.getId());
            rating.getRatingImages().clear();
    		ratingService.delete(rating);

    	}
        for (Voucher voucher : user.getVouchers()) {
    		voucher.getUsers().remove(user);
    	}
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
                           @RequestParam(value = "image") MultipartFile multipartFile,
                           Model model) throws IOException {
        if (result.hasErrors()) {
            List<Role> listRole = roleRepository.findAll();
            model.addAttribute("roles", listRole);

            return "user/user_form";
        }

        String fileName = id == null || (multipartFile != null && !multipartFile.isEmpty())
                ? StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()))
                : userService.getUserById(id).getPhotos();
        user.setPhotos(fileName);
        User savedUser = userService.updateUser(user);
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String uploadDir = "user_photos/" + savedUser.getId();
            FileUploadUtil.deleteAllFiles(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        return "redirect:/admin/users";
    }

    @GetMapping(path = {"/search"})
    public String searchProducts(Model model,
                                 @RequestParam(required = false, defaultValue = "") String keyword,
                                 @RequestParam int page) {
        List<User> userList = null;

        if (keyword != null) {
            Page<User> userPage = userService.searchUserByKeyword(keyword, page);
            userList = userPage.getContent();
            int totalPages = userPage.getTotalPages();
            long totalItems = userPage.getTotalElements();

            model.addAttribute("listUser", userList);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalItems", totalItems);
            model.addAttribute("currentPage", page);
        } else {
            userService.listAll();
        }
        model.addAttribute("listUser", userList);
        return "user/admin_users";
    }
}

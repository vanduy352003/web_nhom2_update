package hcmute.vn.springonetomany.Controller;

import hcmute.vn.springonetomany.Custom.CustomUser;
import hcmute.vn.springonetomany.Custom.CustomUserDetails;
import hcmute.vn.springonetomany.Custom.Oauth2.CustomOAuth2User;
import hcmute.vn.springonetomany.Entities.Category;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Service.CategoryService;
import hcmute.vn.springonetomany.Service.ProductService;
import hcmute.vn.springonetomany.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping({"/home", "/"})
    public String viewHomePage(Model model, @AuthenticationPrincipal CustomUser loggedUser, HttpSession session) {
        List<Category> listCategories = categoryService.listAll();
        List<Product> productList = productService.findAll();

        List<Category> sortedCategories = listCategories.stream()
                .sorted(Comparator.comparing((Category category) -> category.getProducts().size()).reversed())
                .toList();
        List<Product> sortedProducts = productList.stream()
                .sorted(Comparator.comparing((Product product) -> product.getPrice().floatValue()).reversed())
                .toList().subList(0, Math.min(productList.size(), 4));
        List<Product> sortedProductsByCreatedTime = productList.stream()
                .sorted(Comparator.comparing((Product product) -> product.getCreatedAt().getTime()).reversed())
                .toList();

        if (loggedUser != null) {
            User user = userService.findUserByEmail(loggedUser.getEmail());

            session.setAttribute("user", user);
        }

        model.addAttribute("listCategories", sortedCategories);
        model.addAttribute("sortedCategories", sortedCategories.subList(0, Math.min(listCategories.size(), 6)));

        model.addAttribute("sortedProducts", sortedProducts);
        model.addAttribute("sortedProductsByCreatedTime", sortedProductsByCreatedTime.subList(0, Math.min(productList.size(), 6)));
        return "index";
    }
}

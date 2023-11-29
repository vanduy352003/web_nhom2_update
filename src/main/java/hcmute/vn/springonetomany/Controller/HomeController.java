package hcmute.vn.springonetomany.Controller;

import hcmute.vn.springonetomany.Entities.Category;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Service.CategoryService;
import hcmute.vn.springonetomany.Service.ProductService;
import hcmute.vn.springonetomany.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping({"**/home", "/"})
    public String viewHomePage(Model model) {
        List<Category> listCategories = categoryService.listAll();
        List<Product> productList = productService.findAll();

        List<Category> sortedCategories = listCategories.stream()
                .sorted(Comparator.comparing((Category category) -> category.getProducts().size()).reversed())
                .toList().subList(0, 6);
        List<Product> sortedProducts = productList.stream()
                .sorted(Comparator.comparing((Product product) -> product.getPrice().floatValue()).reversed())
                .toList().subList(0, 4);

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("sortedCategories", sortedCategories);

        model.addAttribute("sortedProducts", sortedProducts);
        model.addAttribute("productList", productList);

        return "index";
    }

}

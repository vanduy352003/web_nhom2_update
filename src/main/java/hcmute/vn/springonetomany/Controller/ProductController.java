package hcmute.vn.springonetomany.Controller;

import hcmute.vn.springonetomany.Entities.Category;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Repository.IUserRepository;
import hcmute.vn.springonetomany.Entities.Rating;
import hcmute.vn.springonetomany.Service.CategoryService;
import hcmute.vn.springonetomany.Service.ProductService;
import hcmute.vn.springonetomany.Service.RatingService;
import hcmute.vn.springonetomany.Ultis.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private RatingService ratingService;
    //edit
    @Autowired
    private CategoryService categoryService;
    //edit
    @GetMapping("")
    public String getOnePage(Model model,
                             @RequestParam(required = false, defaultValue = "1") int page, 
                             @RequestParam(required = false, defaultValue = "name") String sortField, 
                             @RequestParam(required = false, defaultValue = "asc") String sortDir,
                             HttpSession session) {
        Page<Product> productPage = productService.findPage(page, sortField, sortDir);
        List<Product> productList = productPage.getContent();
        
        List<Category> listCategories = categoryService.listAll();
        
        
        int totalPages = productPage.getTotalPages();
        long totalItems = productPage.getTotalElements();

        model.addAttribute("productList", productList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
      
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);
        
        model.addAttribute("listCategories", listCategories);
        return "product/products";
    }

    @GetMapping(path = {"/search"})
    public String searchProducts(Model model, @RequestParam(required = false, defaultValue = "") String keyword, @RequestParam int page) {
        List<Product> productList = null;

        if (keyword != null) {
            Page<Product> productPage = productService.searchProducts(keyword, page);
            productList = productPage.getContent();
            int totalPages = productPage.getTotalPages();
            long totalItems = productPage.getTotalElements();

            model.addAttribute("productList", productList);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalItems", totalItems);
            model.addAttribute("currentPage", page);
        } else {
            productService.findAll();
        }
        model.addAttribute("productList", productList);
        return "product/products";
    }

    @GetMapping({"/category/{id}"})
    public String showProductsByCategoryId(@PathVariable("id") int id, @RequestParam int page, Model model) throws Exception {
        Page<Product> productPage = productService.getProductsByCategory_Id(id, page);
        List<Product> productList = productPage.getContent();
        int totalPages = productPage.getTotalPages();
        long totalItems = productPage.getTotalElements();

        model.addAttribute("productList", productList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", page);
        return "product/products";
    }

    @GetMapping("/detail/{id}")
    public String showProductDetail(@PathVariable("id") int id, @RequestParam(name = "reviewPage", defaultValue = "1") int reviewPage, Model model) {
        try {
            Product product = productService.findById(id);
            Page<Rating> ratingPage = ratingService.getRatingsByProduct_Id(id, reviewPage);
            List<Rating> listRating = ratingPage.getContent();
            int totalPages = ratingPage.getTotalPages();
            long totalItems = ratingPage.getTotalElements();
            Rating rating = new Rating();
            int numberOfRating = 0;
            int ratingPoint = 0;
            if (!product.getRatings().isEmpty()) {
            	numberOfRating = product.getRatings().size();
                ratingPoint = product.getRatingPoint();
            }
            Optional<Category> optionalCategory  = categoryService.getCategory(product.getCategory().getId());
            if (optionalCategory.isPresent()) {
                Category currentCategory = optionalCategory.get();
                Set<Product> categoryProducts = currentCategory.getProducts();
                List<Product> categoryProductList = new ArrayList<>(categoryProducts);
                categoryProductList.remove(product);
                int maxProductsCount = Math.min(4, categoryProductList.size());
                List<Product> relevantProducts = categoryProductList.subList(0, maxProductsCount);
                model.addAttribute("relevantProducts", relevantProducts);
            }
            model.addAttribute("listRating", listRating);
            model.addAttribute("ratingPoint", ratingPoint);
            model.addAttribute("numberOfRating", numberOfRating);
            model.addAttribute("product", product);
            model.addAttribute("rating", rating);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalItems", totalItems);
            model.addAttribute("currentPage", reviewPage);
            
        } catch (Exception e) {
            return "redirect:/products";
        }
        return "detail";
    }
    
}
package hcmute.vn.springonetomany.Controller;

import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String getOnePage(Model model, @RequestParam(required = false, defaultValue = "1") int page) {
        Page<Product> productPage = productService.findPage(page);
        List<Product> productList = productPage.getContent();
        int totalPages = productPage.getTotalPages();
        long totalItems = productPage.getTotalElements();

        model.addAttribute("productList", productList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
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

    @GetMapping({"/products/category/{id}"})
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

    @GetMapping("/products/detail/{id}")
    public String showProductDetail(@PathVariable("id") int id, Model model) {
        try {
            Product product = productService.findById(id);
            model.addAttribute("product", product);
        } catch (Exception e) {
            return "redirect:/products";
        }
        return "detail";
    }
}

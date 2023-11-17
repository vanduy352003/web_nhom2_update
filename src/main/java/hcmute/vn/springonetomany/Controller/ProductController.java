package hcmute.vn.springonetomany.Controller;

import hcmute.vn.springonetomany.Entities.Category;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Service.CategoryService;
import hcmute.vn.springonetomany.Service.ProductService;
import hcmute.vn.springonetomany.Ultis.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

//    @GetMapping("**/products")
//    public String listAllProduct(Model model) {
//        List<Product> productList = productService.listAll();
//        model.addAttribute("productList", productList);
//        return "products";
//    }

    @GetMapping("**/products")
    public String getFirstPage(Model model) {
        return getOnePage(model, 1);
    }

    @GetMapping("**/products/{pageNumber}")
    public String getOnePage(Model model, @PathVariable("pageNumber") int currentPage) {
        Page<Product> productPage = productService.findPage(currentPage);
        int totalPages = productPage.getTotalPages();
        long totalItems = productPage.getTotalElements();
        List<Product> productList = productPage.getContent();

        model.addAttribute("productList", productList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);

        return "products";
    }

    @RequestMapping(path = {"**/search/{pageNumber}"})
    public String searchProducts(Model model, @RequestParam String keyword, @PathVariable("pageNumber") int currentPage) {
        List<Product> productList = null;
        if (keyword != null) {
            Page<Product> productPage = productService.searchProducts(keyword, currentPage);
            productList = productPage.getContent();
            int totalPages = productPage.getTotalPages();
            long totalItems = productPage.getTotalElements();

            model.addAttribute("productList", productList);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalItems", totalItems);
            model.addAttribute("currentPage", currentPage);
        } else {
            productService.findAll();
        }
        model.addAttribute("productList", productList);
        return "products";
    }

    @GetMapping("/admin/products/new")
    public String showNewProductForm(Model model) {
        List<Category> listCategory = categoryService.listAll();
        model.addAttribute("product", new Product());
        model.addAttribute("listCategory", listCategory);
        return "product_form";
    }

    @GetMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") int id, Model model) {
        try {
            Product product = productService.findById(id);
            List<Category> listCategory = categoryService.listAll();
            model.addAttribute("listCategory", listCategory);
            model.addAttribute("product", product);
            return "product_form";
        } catch (Exception e) {
            return "redirect:/admin/products";
        }
    }


    @GetMapping("**/products/category/{id}/{pageNumber}")
    public String showProductsByCategoryId(@PathVariable("id") int id, @PathVariable("pageNumber") int currentPage, Model model) throws Exception {
//        if (categoryService.findById(id).getProducts().isEmpty()) {
//            return "redirect:/home";
//        }
//            Category category = categoryService.findById(id);
//            Set<Product> productList = category.getProducts();
//            model.addAttribute("productList", productList);
        Page<Product> productPage = productService.getProductsByCategory_Id(id, currentPage);
        List<Product> productList = productPage.getContent();
        int totalPages = productPage.getTotalPages();
        long totalItems = productPage.getTotalElements();

        model.addAttribute("productList", productList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        return "products";
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

    @PostMapping("/admin/products/save")
    private String saveProduct(Product product, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        product.setPhotos(fileName);
        Product savedProduct = productService.getNewProduct(product);

        String uploadDir = "product_photos/" + savedProduct.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return "redirect:/admin/products";
    }
}

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

import java.util.List;


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

//    @GetMapping("/products")
//    public String getFirstPage(Model model) {
//        return getOnePage(model, 1);
//    }
//
//    @GetMapping("**/products/{pageNumber}")
//    public String getOnePage(Model model, @PathVariable("pageNumber") int currentPage) {
//        Page<Product> productPage = productService.findPage(currentPage);
//        int totalPages = productPage.getTotalPages();
//        long totalItems = productPage.getTotalElements();
//        List<Product> productList = productPage.getContent();
//
//        model.addAttribute("productList", productList);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("totalItems", totalItems);
//        model.addAttribute("currentPage", currentPage);
//
//        return "products";
//    }

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

    @GetMapping(path = {"**/search"})
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

//    @GetMapping("/admin/products/new")
//    public String showNewProductForm(Model model) {
//        List<Category> listCategory = categoryService.listAll();
//        model.addAttribute("product", new Product());
//        model.addAttribute("listCategory", listCategory);
//        return "product/product_form";
//    }
//
//    @GetMapping("/admin/products/delete/{id}")
//    public String deleteProduct(@PathVariable("id") int id) {
//        productService.deleteById(id);
//        return "redirect:/admin/products";
//    }

//    @GetMapping("/admin/products/edit/{id}")
//    public String showEditProductForm(@PathVariable("id") int id, Model model) {
//        try {
//            Product product = productService.findById(id);
//            List<Category> listCategory = categoryService.listAll();
//            model.addAttribute("listCategory", listCategory);
//            model.addAttribute("product", product);
//            return "product/product_form";
//        } catch (Exception e) {
//            return "redirect:/admin/products";
//        }
//    }

//    @GetMapping("**/products/category/{id}")
//    public String getFirstPageByCategoryId(@PathVariable("id") int id, Model model) throws Exception {
//        return showProductsByCategoryId(id, 1, model);
//    }
    @GetMapping({"**/products/category/{id}"})
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

//    @PostMapping("/admin/products/save")
//    private String saveProduct(Product product, @RequestParam("image") MultipartFile multipartFile) throws IOException {
//        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//        product.setPhotos(fileName);
//        Product savedProduct = productService.getNewProduct(product);
//
//        String uploadDir = "product_photos/" + savedProduct.getId();
//        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//
//        return "redirect:/admin/products";
//    }
}

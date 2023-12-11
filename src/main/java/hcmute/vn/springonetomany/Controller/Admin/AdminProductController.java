package hcmute.vn.springonetomany.Controller.Admin;

import hcmute.vn.springonetomany.Entities.Category;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.ProductImages;
import hcmute.vn.springonetomany.Service.CategoryService;
import hcmute.vn.springonetomany.Service.ProductImagesService;
import hcmute.vn.springonetomany.Service.ProductService;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductImagesService productImagesService;
    //edit
    @GetMapping("")
    public String showProductsPage(Model model, @RequestParam(required = false, defaultValue = "1") int page, 
    		@RequestParam(required = false, defaultValue = "name") String sortField, 
            @RequestParam(required = false, defaultValue = "asc") String sortDir) {
//        List<Product> listProduct = productService.findAll();
        Page<Product> listProduct = productService.findPage(page, sortField, sortDir);
        int totalPages = listProduct.getTotalPages();
        long totalItems = listProduct.getTotalElements();

        model.addAttribute("listProduct", listProduct);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
      
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "product/admin_products";
    }

    @GetMapping("/new")
    public String showNewProductForm(Model model) {
        List<Category> listCategory = categoryService.listAll();
        model.addAttribute("product", new Product());
        model.addAttribute("listCategory", listCategory);
        return "product/product_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") int id, Model model) {
        try {
            Product product = productService.findById(id);
            List<Category> listCategory = categoryService.listAll();
            model.addAttribute("listCategory", listCategory);
            model.addAttribute("product", product);
            return "product/product_form";
        } catch (Exception e) {
            return "redirect:/admin/products";
        }
    }

    @PostMapping("/save")
    private String saveProduct(@Valid Product product,
                               BindingResult result,
                               @RequestParam(value = "image") MultipartFile multipartFile,
                               @RequestParam(value="listImages") List<MultipartFile> multipartFiles,
                               @RequestParam(value = "id", required = false) Integer id) throws Exception {
        if (result.hasErrors()) {
            return "product/product_form";
        }
        String fileName = id == null || (multipartFile != null && !multipartFile.isEmpty())
                ? StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()))
                : productService.findById(id).getPhotos();

        product.setPhotos(fileName);
        Product savedProduct = productService.getNewProduct(product);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String uploadDir = "product_photos/" + savedProduct.getId();
            FileUploadUtil.deleteAllFiles(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        if (!(multipartFiles == null || multipartFiles.isEmpty() || multipartFiles.stream().allMatch(MultipartFile::isEmpty))) {
            for(ProductImages productImages:  savedProduct.getProductImages())
            {
                productImagesService.delete(productImages);
            }
            savedProduct.getProductImages().clear();
            String uploadDir="product_images/"+ savedProduct.getId();
            FileUploadUtil.deleteAllFiles(uploadDir);
            for(MultipartFile productImages: multipartFiles)
            {
                String filename = StringUtils.cleanPath(Objects.requireNonNull(productImages.getOriginalFilename()));
                ProductImages productImage =new ProductImages();
                productImage.setImageUrl(filename);
                productImage.setProduct(savedProduct);
                ProductImages savedProductImages = productImagesService.getNewProductImages(productImage);
                String uploadDirs = uploadDir +"/" + savedProductImages.getId();
                FileUploadUtil.saveFile(uploadDirs, filename, productImages);
                product.getProductImages().add(productImage);

            }
        }
        return "redirect:/admin/products";
    }
}

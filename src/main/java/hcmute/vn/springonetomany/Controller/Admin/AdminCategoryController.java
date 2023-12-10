package hcmute.vn.springonetomany.Controller.Admin;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import hcmute.vn.springonetomany.Entities.Category;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Service.CategoryService;
import hcmute.vn.springonetomany.Ultis.FileUploadUtil;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {
	
	@Autowired
    private CategoryService categoryService;
	
	@GetMapping("")
	public String showCategoryPage(Model model, @RequestParam(required = false, defaultValue = "1") int page) {
//      List<Product> listProduct = productService.findAll();
      Page<Category> listCategory = categoryService.findPage(page);
      int totalPages = listCategory.getTotalPages();
      long totalItems = listCategory.getTotalElements();

      model.addAttribute("listCategory", listCategory);
      model.addAttribute("currentPage", page);
      model.addAttribute("totalPages", totalPages);
      model.addAttribute("totalItems", totalItems);

      return "category/admin_categorys";
  }
	
	@GetMapping("/new")
    public String showNewCategoryForm(Model model) {
        List<Category> listCategory = categoryService.listAll();
        model.addAttribute("category", new Category());
        model.addAttribute("listCategory", listCategory);
        return "category/category_form";
    }
	
	@GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") int id) {
		categoryService.deleteById(id);
        return "redirect:/admin/categories";
    }
	
	@GetMapping("/edit/{id}")
    public String showEditCategoryForm(@PathVariable("id") int id, Model model) {
        try {
            Category category = categoryService.findById(id);
            List<Category> listCategory = categoryService.listAll();
            model.addAttribute("listCategory", listCategory);
            model.addAttribute("category", category);
            return "category/category_form";
        } catch (Exception e) {
            return "redirect:/admin/categories";
        }
    }
	
	@PostMapping("/save")
    private String saveCategory(@Valid Category category,
                               BindingResult result,
                               @RequestParam(value = "image") MultipartFile multipartFile,
                               @RequestParam(value = "id", required = false) Integer id) throws Exception {
        if (result.hasErrors()) {
            return "category/category_form";
        }

        String fileName = id == null || (multipartFile != null && !multipartFile.isEmpty())
                ? StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()))
                : categoryService.findById(id).getPhotos();

        category.setPhotos(fileName);
        Category savedCategory = categoryService.getNewCategory(category);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String uploadDir = "category_photos/" + savedCategory.getId();
            FileUploadUtil.deleteAllFiles(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        return "redirect:/admin/categories";
    }
	
}

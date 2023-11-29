package hcmute.vn.springonetomany.Controller.Admin;

import hcmute.vn.springonetomany.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    private String showAdminPage() {
        return "admin/admin_page";
    }
}

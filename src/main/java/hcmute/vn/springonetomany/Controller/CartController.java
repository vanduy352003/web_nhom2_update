package hcmute.vn.springonetomany.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/cart")
@Controller
public class CartController {
    @GetMapping("/{id}")
    public String showCartPage(@PathVariable("id") Integer id){
        return "cart/cart";
    }
}

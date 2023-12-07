package hcmute.vn.springonetomany.Controller;

import hcmute.vn.springonetomany.Entities.Cart;
import hcmute.vn.springonetomany.Entities.CartItem;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Repository.IProductRepository;
import hcmute.vn.springonetomany.Repository.IUserRepository;
import hcmute.vn.springonetomany.Service.CartItemService;
import hcmute.vn.springonetomany.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/cart")
@Controller
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    CartItemService cartItemService;
    @Autowired
    IProductRepository productRepository;
    @GetMapping("")
    public String showCartPage(Model model, HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        Cart cart = cartService.getCartByUserId(user.getId());
//        Set<CartItem> cartItemList = cart.getCartItems();
        List<CartItem> cartItemList = cartItemService.listCartItemByCartId(cart.getId());
        cartService.recalculateCartTotal(cart.getId());

        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("total", cart.getPriceFormatted());
        // Cập nhật user trong session
        user = userRepository.findById(user.getId()).orElse(null);
        session.setAttribute("user", user);
        return "cart/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId,
                            @RequestParam(required = false, defaultValue = "1") Integer quantity,
                            HttpSession session) {
        // Lấy user
        User user = (User) session.getAttribute("user");

        // Them vao gio hang
        cartService.addToCart(user.getCart().getId(), productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam Integer cartItemId,
                             @RequestParam(required = false, defaultValue = "1") Integer quantity) {
        CartItem cartItem = cartItemService.findById(cartItemId);
        Product product = cartItem.getProduct();
        int oldQuantity = cartItem.getQuantity();

        if (product.getInventory() + oldQuantity - quantity >= 0) {
            cartItem.setQuantity(quantity);
            cartItemService.save(cartItem);
            product.setInventory(product.getInventory() + oldQuantity - quantity);
            productRepository.save(product);

            return "redirect:/cart";
        }

        return "redirect:/cart?error=true";
    }

    @GetMapping("/delete/{id}")
    public String deleteCartItem(@PathVariable("id") Integer id) {
        cartItemService.deleteCartItemById(id);

        return "redirect:/cart";
    }
}

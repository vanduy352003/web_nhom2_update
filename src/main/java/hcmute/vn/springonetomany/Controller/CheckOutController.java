package hcmute.vn.springonetomany.Controller;

import hcmute.vn.springonetomany.Custom.CustomUser;
import hcmute.vn.springonetomany.Custom.CustomUserDetails;
import hcmute.vn.springonetomany.Custom.Oauth2.CustomOAuth2User;
import hcmute.vn.springonetomany.Entities.Cart;
import hcmute.vn.springonetomany.Entities.CartItem;
import hcmute.vn.springonetomany.Entities.Category;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Repository.IProductRepository;
import hcmute.vn.springonetomany.Repository.IUserRepository;
import hcmute.vn.springonetomany.Service.CartItemService;
import hcmute.vn.springonetomany.Service.CartService;
import hcmute.vn.springonetomany.Service.CategoryService;
import hcmute.vn.springonetomany.Service.ProductService;
import hcmute.vn.springonetomany.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CheckOutController {
	 @Autowired
	    CartService cartService;
	 @Autowired
	    IUserRepository userRepository;
	 @Autowired
	    CartItemService cartItemService;
	 @Autowired
	    IProductRepository productRepository;

    @GetMapping("/checkout")
    public String viewHomePage(Model model, @AuthenticationPrincipal CustomUser loggedUser, HttpSession session) {
    	 User user = (User) session.getAttribute("user");
         Cart cart = cartService.getCartByUserId(user.getId());
//         Set<CartItem> cartItemList = cart.getCartItems();
         List<CartItem> cartItemList = cartItemService.listCartItemByCartId(cart.getId());
         cartService.recalculateCartTotal(cart.getId());
         
         model.addAttribute("cartItemList", cartItemList);
         model.addAttribute("quantity",cartItemList.size());
         model.addAttribute("total", cart.getPriceFormatted());
         // Cập nhật user trong session
         user = userRepository.findById(user.getId()).orElse(null);
         session.setAttribute("user", user);

        return "checkout";
    }
}

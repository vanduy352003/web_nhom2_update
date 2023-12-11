package hcmute.vn.springonetomany.Controller;

import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import hcmute.vn.springonetomany.Entities.Cart;
import hcmute.vn.springonetomany.Entities.CartItem;
import hcmute.vn.springonetomany.Entities.Orders;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Repository.IProductRepository;
import hcmute.vn.springonetomany.Repository.IUserRepository;
import hcmute.vn.springonetomany.Service.CartItemService;
import hcmute.vn.springonetomany.Service.CartService;
import hcmute.vn.springonetomany.Service.PaypalService;

@Controller
public class PaypalController {

	@Autowired
	PaypalService service;
    @Autowired
    CartService cartService;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    CartItemService cartItemService;
    @Autowired
    IProductRepository productRepository;

	public static final String SUCCESS_URL = "pay/success";
	public static final String CANCEL_URL = "pay/cancel";

	@GetMapping("/paypal")
	public String paypal_home(Model model, HttpSession session) {
		
		User user = (User) session.getAttribute("user");
        Cart cart = cartService.getCartByUserId(user.getId());
//        Set<CartItem> cartItemList = cart.getCartItems();
        List<CartItem> cartItemList = cartItemService.listCartItemByCartId(cart.getId());
        cartService.recalculateCartTotal(cart.getId());
        
        double exchangeRate = 0.000043;
        double totalInUSD = cart.getTotal() * exchangeRate;

        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("total", cart.getPriceFormatted());
        model.addAttribute("total_num", cart.getTotal());
        model.addAttribute("total_num_usd", totalInUSD);
        // Cập nhật user trong session
        user = userRepository.findById(user.getId()).orElse(null);
        session.setAttribute("user", user);
		
		return "payment/paypal_home";
	}
	


	@PostMapping("/pay")
	public String payment(@ModelAttribute("order") Orders order) {
		try {
			Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
					order.getIntent(), order.getDescription(), "http://localhost:8080/" + CANCEL_URL,
					"http://localhost:8080/" + SUCCESS_URL);
			for(Links link:payment.getLinks()) {
				if(link.getRel().equals("approval_url")) {
					return "redirect:"+link.getHref();
				}
			}
			
		} catch (PayPalRESTException e) {
		
			e.printStackTrace();
		}
		return "redirect:/";
	}
	
	 @GetMapping(value = CANCEL_URL)
	    public String cancelPay() {
	        return "payment/cancel";
	    }

	    @GetMapping(value = SUCCESS_URL)
	    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
	        try {
	            Payment payment = service.executePayment(paymentId, payerId);
	            System.out.println(payment.toJSON());
	            if (payment.getState().equals("approved")) {
	                return "payment/success";
	            }
	        } catch (PayPalRESTException e) {
	         System.out.println(e.getMessage());
	        }
	        return "redirect:/";
	    }

}
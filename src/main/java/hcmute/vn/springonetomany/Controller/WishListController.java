package hcmute.vn.springonetomany.Controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Entities.WishList;
import hcmute.vn.springonetomany.Entities.WishListItem;
import hcmute.vn.springonetomany.Repository.IUserRepository;
import hcmute.vn.springonetomany.Service.WishListItemService;
import hcmute.vn.springonetomany.Service.WishListService;

@Controller
public class WishListController {
	@Autowired
	WishListService wishListService;
	@Autowired
	WishListItemService wishListItemService;
	@Autowired
	IUserRepository userRepository;

	@GetMapping("/wishlist")
	public String showWishList(Model model, HttpSession session) throws Exception {
		User user = (User) session.getAttribute("user");
		WishList wishList = wishListService.getWishListByUserId(user.getId());
		List<WishListItem> wishListItem = wishListItemService.getWishListItemsByWishlistId(wishList.getId());

		model.addAttribute("wishlistItem", wishListItem);
		// Cập nhật user trong session
		user = userRepository.findById(user.getId()).orElse(null);
		session.setAttribute("user", user);
		return "wishlist/wishlist";
	}

	@GetMapping("/addtowishlist/{id}")
	public String addToWishList(@PathVariable("id") Integer productId, HttpSession session) {
		// Lấy user
		User user = (User) session.getAttribute("user");

		// Them vao gio hang
		wishListService.addToWishList(user.getWishList().getId(), productId);
		return "redirect:/?status=success";
	}
	@GetMapping("/removewishlistitem/{id}")
	public String removeItem(@PathVariable("id") int id) {
		try {
			wishListItemService.deleteWishListItemById(id);
			return "redirect:/wishlist?status_success='success'";
		} catch (Exception e) {
			return "redirect:/wishlist?status_error='success'";
		}
		
	}

	@GetMapping("/clearwishlist")
	public String clearWishList() {
		wishListItemService.clearWishList();
		return "redirect:/wishlist";
		
	}



}

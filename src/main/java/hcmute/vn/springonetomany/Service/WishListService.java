package hcmute.vn.springonetomany.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Entities.WishList;
import hcmute.vn.springonetomany.Entities.WishListItem;
import hcmute.vn.springonetomany.Repository.IProductRepository;
import hcmute.vn.springonetomany.Repository.IUserRepository;
import hcmute.vn.springonetomany.Repository.IWishListItemRepository;
import hcmute.vn.springonetomany.Repository.IWishListRepository;



@Service
public class WishListService {
	
	@Autowired
	private IWishListRepository wishListRepository;
	@Autowired
	private IWishListItemRepository wishListItemRepository;
	@Autowired
	private IProductRepository productRepository;
	@Autowired
	private IUserRepository  userRepository;
	
	public List<WishList> listAllWishList() {
        return wishListRepository.findAll();
    }
	public WishList findWishListById(Integer id) throws Exception {
        Optional<WishList> result = wishListRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new Exception("Could not find product");
    }
	 public WishList getWishListByUserId(Long user_id) {
	        WishList wishList = wishListRepository.getWishListByUserID(user_id);
	        if (wishList == null) {
	            User user = userRepository.getById(user_id);
	            wishList = new WishList();
	            wishList.setUser(user);
	           wishListRepository.save(wishList);
	        }
	        return wishListRepository.getWishListByUserID(user_id);
	    }
	 public void saveWishList(WishList wishList) {

	        wishListRepository.save(wishList);
	    }
	 public void addToWishList(Integer wishListId, Integer productId) {
	        Product product = productRepository.findById(productId).orElse(null);
	        WishList wishList = wishListRepository.getById(wishListId);
	        if (product != null) {
	            WishListItem wishListItem = wishListItemRepository.findByCartIdAndProductId(wishList.getId(), product.getId());
	            if (wishListItem == null) {
	            	wishListItem = new WishListItem();
	            	wishListItem.setWishList(wishList);
	            	wishListItem.setProduct(product);
	            	
	            } 
	            wishListItemRepository.save(wishListItem);
	            productRepository.save(product);
	        }
	    }
	public void deleteWishListItemById(Integer id) {
		WishList wishList = wishListRepository.getById(id);
		/*
		 * Product product = wishListItem.getProduct(); productRepository.save(product);
		 */
        wishListRepository.deleteById(id);
	}
	public void clearWishList() {
		wishListRepository.deleteAll();
		
	}
	public boolean isProductInWishList(Integer id, Integer productId) {
		Optional<WishList> optionalWishList = wishListRepository.findById(id);

        // Nếu danh sách yêu thích tồn tại
        if (optionalWishList.isPresent()) {
        	return optionalWishList.get().getItems().stream()
                    .anyMatch(wishListItem -> wishListItem.getProduct().getId().equals(productId));
        }
        return false;
	}
}

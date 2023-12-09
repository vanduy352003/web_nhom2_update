package hcmute.vn.springonetomany.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hcmute.vn.springonetomany.Entities.WishList;
import hcmute.vn.springonetomany.Entities.WishListItem;
import hcmute.vn.springonetomany.Repository.IWishListItemRepository;

@Repository
public class WishListItemService {
	@Autowired
	IWishListItemRepository wishListRepository;
	
	@Autowired
	IWishListItemRepository wishListItemRepository;
	
	public void deleteWishListItemById(int id){
		wishListItemRepository.deleteById(id);
	}
	public List<WishListItem> getWishListItemsByWishlistId(Integer wishlistId) {
        return wishListItemRepository.findByWishListID(wishlistId);
    }
	public void clearWishList() {
		wishListItemRepository.deleteAllItem();;
		
	}
	 public WishListItem findByCartIdAndProductId(int wishListId, int productId) {
	        return wishListItemRepository.findByCartIdAndProductId(wishListId, productId);
	    }
	
	
}

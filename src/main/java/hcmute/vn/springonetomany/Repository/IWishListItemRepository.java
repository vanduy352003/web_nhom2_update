package hcmute.vn.springonetomany.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hcmute.vn.springonetomany.Entities.WishListItem;

@Repository
public interface IWishListItemRepository extends JpaRepository<WishListItem, Integer>{
	@Query("select w from WishListItem w where w.wishList.id=:wishlist_id")
	List<WishListItem> findByWishListID(@Param("wishlist_id") Integer wishlist_id);

	@Query("select w from WishListItem w where w.wishList.id = :wishlist_id and w.product.id = :product_Id")
	WishListItem findByCartIdAndProductId(@Param("wishlist_id") Integer wishListId, @Param("product_Id") Integer productId);
	
	@Transactional
    @Modifying
    @Query("delete from WishListItem w where w.id = ?1")
    void deleteById(int id);
	
	@Transactional
    @Modifying
    @Query("delete from WishListItem")
	void deleteAllItem();
}

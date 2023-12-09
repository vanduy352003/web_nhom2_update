package hcmute.vn.springonetomany.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hcmute.vn.springonetomany.Entities.WishList;

@Repository
public interface IWishListRepository extends JpaRepository<WishList,Integer>{
	@Query("select w from WishList w where w.user.id= ?1")
	WishList getWishListByUserID(Long user_id);

	
}

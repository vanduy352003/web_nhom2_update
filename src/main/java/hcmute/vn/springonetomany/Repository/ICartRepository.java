package hcmute.vn.springonetomany.Repository;

import hcmute.vn.springonetomany.Entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Integer> {
    @Query("select c from Cart c where c.user.id = ?1")
    Cart getCartByUserId(Long user_id);
}
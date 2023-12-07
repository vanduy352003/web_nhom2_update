package hcmute.vn.springonetomany.Repository;

import hcmute.vn.springonetomany.Entities.Cart;
import hcmute.vn.springonetomany.Entities.CartItem;
import hcmute.vn.springonetomany.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query("select c from CartItem c where c.cart = ?1 and c.product = ?2")
    CartItem findByCartAndProduct(Cart cart, Product product);

    @Query("select c from CartItem c where c.cart.id = ?1")
    List<CartItem> findByCartId(int cartId);

    @Query("select c from CartItem c where c.cart.id = ?1 and c.product.id = ?2")
    CartItem findByCartIdAndProductId(Integer cartId, Integer productId);

    @Transactional
    @Modifying
    @Query("delete from CartItem c where c.id = ?1")
    void deleteById(int id);

    @Transactional
    @Modifying
    @Query("delete from CartItem c where c.product.id = ?1")
    void deleteAllByProductId(int id);
}
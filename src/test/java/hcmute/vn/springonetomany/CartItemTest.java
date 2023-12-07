package hcmute.vn.springonetomany;

import hcmute.vn.springonetomany.Entities.CartItem;
import hcmute.vn.springonetomany.Repository.ICartItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CartItemTest {
    @Autowired
    ICartItemRepository cartItemRepository;

    @Test
    public void deleteCartItem() {
        CartItem cartItem =  cartItemRepository.findById(1).orElse(null);
        List<CartItem> cartItemList = cartItemRepository.findByCartId(1);
        boolean isTrue = cartItem == null;
//        cartItem.setQuantity(5);
        cartItemRepository.deleteById(1);
        boolean isNull = cartItemRepository.findById(1).orElse(null) == null;

    }
}

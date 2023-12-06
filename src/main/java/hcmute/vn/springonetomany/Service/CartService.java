package hcmute.vn.springonetomany.Service;

import hcmute.vn.springonetomany.Entities.Cart;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Repository.ICartRepository;
import hcmute.vn.springonetomany.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    ICartRepository cartRepository;
    @Autowired
    IUserRepository userRepository;

    public List<Cart> listAllCart() {
        return cartRepository.findAll();
    }

    public Cart findCartById(Integer id) throws Exception {
        Optional<Cart> result = cartRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new Exception("Could not find product");
    }

    public Cart getCartByUserId(Long user_id) {
        Cart cart = cartRepository.getCartByUserId(user_id);
        if (cart == null) {
            User user = userRepository.getById(user_id);
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        return cartRepository.getCartByUserId(user_id);
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

}

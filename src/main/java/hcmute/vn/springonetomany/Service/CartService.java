package hcmute.vn.springonetomany.Service;

import hcmute.vn.springonetomany.Entities.Cart;
import hcmute.vn.springonetomany.Entities.CartItem;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Repository.ICartItemRepository;
import hcmute.vn.springonetomany.Repository.ICartRepository;
import hcmute.vn.springonetomany.Repository.IProductRepository;
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
    @Autowired
    IProductRepository productRepository;
    @Autowired
    ICartItemRepository cartItemRepository;
    @Autowired
    CartItemService cartItemService;

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
        recalculateCartTotal(cart.getId());
    }


    public void addToCart(Integer cartId, Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId).orElse(null);
        Cart cart = cartRepository.getById(cartId);
        if (product != null) {
            CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

            // Chưa có cart item
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }
            cartItemRepository.save(cartItem);
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            this.recalculateCartTotal(cartId);
        }
    }

    public void recalculateCartTotal(Integer cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart != null) {
            double newTotal = 0;
            for (CartItem item : cart.getCartItems()) {
                newTotal += item.getProduct().getPrice() * item.getQuantity();
            }
            cart.setTotal(newTotal);
            cartRepository.save(cart);
        }
    }
}

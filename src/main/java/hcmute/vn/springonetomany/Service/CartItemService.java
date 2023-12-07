package hcmute.vn.springonetomany.Service;

import hcmute.vn.springonetomany.Entities.CartItem;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Repository.ICartItemRepository;
import hcmute.vn.springonetomany.Repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {
    @Autowired
    ICartItemRepository cartItemRepository;
    @Autowired
    IProductRepository productRepository;

    public void deleteCartItemById(int id){
        CartItem cartItem = cartItemRepository.getById(id);
        Product product = cartItem.getProduct();
        product.setInventory(product.getInventory() + cartItem.getQuantity());

        productRepository.save(product);
        cartItemRepository.deleteById(id);

    }

    public List<CartItem> listCartItemByCartId(int cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    public CartItem findByCartIdAndProductId(int cartId, int productId) {
        return cartItemRepository.findByCartIdAndProductId(cartId, productId);
    }

    public CartItem findById(Integer cartItemId) {
        return cartItemRepository.getById(cartItemId);
    }

    public void save(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }
}

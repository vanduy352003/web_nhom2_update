package hcmute.vn.springonetomany.Service;

import hcmute.vn.springonetomany.Entities.*;
import hcmute.vn.springonetomany.Enum.AuthProvider;
import hcmute.vn.springonetomany.Repository.IRoleRepository;
import hcmute.vn.springonetomany.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepo;

    @Autowired
    IRoleRepository roleRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    WishListService wishListService;
    
    @Autowired
    CartService cartService;

    int PAGE_SIZE = 2;

    public void registerDefaultUser(User user) {
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Role roleUser = roleRepo.findByName("User");
        user.addRole(roleUser);
        user.setAuthProvider(AuthProvider.DATABASE);
        encodePassword(user);
        userRepo.save(user);

        //Tạo giỏ hàng cho người dùng
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        cartService.saveCart(cart);
        //Tạo wishlist cho người dùng
        WishList wishList=new WishList();
        wishList.setUser(user);
        user.setWishList(wishList);
        wishListService.saveWishList(wishList);
    }

    public List<User> listAll() {
        return userRepo.findAll();
    }

    public Page<User> findPage(int pageNumber, String fieldName, String sortDir) {
        Sort sort = Sort.by(fieldName).descending();
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, sort);
        return userRepo.findAll(pageable);
    }

    public User getUserById(Long id) {
        Optional<User> existedUser = userRepo.findById(id);
        if (existedUser.isPresent()) {
            return existedUser.get();
        }
        throw new UsernameNotFoundException("Không tồn tại người dùng");
    }

    public List<Role> listRoles() {
        return roleRepo.findAll();
    }

    public void save(User user) {
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        encodePassword(user);
        userRepo.save(user);
    }

    public void saveOauth2(User user) {
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepo.save(user);
        //Tạo giỏ hàng cho người dùng
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        cartService.saveCart(cart);
        
      //Tạo wishlist cho người dùng
        WishList wishList=new WishList();
        wishList.setUser(user);
        user.setWishList(wishList);
        wishListService.saveWishList(wishList);
        
    }

    public User getNewUser(User user) {
        encodePassword(user);
        return userRepo.save(user);
    }

    public User updateUser(User user) {
        User existingUser = userRepo.findById(user.getId()).orElse(null);

        if (existingUser != null) {
            // Check if the password field is empty
            if (user.getPassword().isEmpty()) {
                // If empty, keep the old password
                user.setPassword(existingUser.getPassword());
            } else {
                // If not empty, encode the new password
                encodePassword(user);
            }
            user.setAuthProvider(existingUser.getAuthProvider());
        }
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return userRepo.save(user);
    }

    public void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public void deleteUserById(Long id) {
        if (userRepo.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng");
        }
        userRepo.deleteById(id);
    }

    public boolean existsUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        return user != null;
    }

    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public boolean checkPassword(String email, String password) {
        User user = userRepo.findByEmail(email);
        return  user.getPassword().equals(password);
    }

    public void updateAuthenticationType(String username, String oauth2ClientName) {
        AuthProvider authType = AuthProvider.valueOf(oauth2ClientName.toUpperCase());
        userRepo.updateAuthenticationProvider(username, authType);
    }

    public Page<User> searchUserByKeyword(String keyword, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);
        return userRepo.getUserByKeyword(keyword, pageable);
    }
}

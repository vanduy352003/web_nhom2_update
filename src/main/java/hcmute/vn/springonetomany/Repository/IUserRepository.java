package hcmute.vn.springonetomany.Repository;

import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Enum.AuthProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.email = ?1")
	public User findByEmail(String email);

	@Modifying
	@Query("UPDATE User u SET u.authProvider = ?2 WHERE u.email = ?1")
	public void updateAuthenticationProvider(String username, AuthProvider authProvider);

	@Query("select u from User u where concat('%', u.email, ' ', u.lastName, ' ', u.firstName, ' ', u.phone ,'%') like concat('%', ?1, '%')")
	Page<User> getUserByKeyword(String keyword, Pageable pageable);

}

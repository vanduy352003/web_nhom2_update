package hcmute.vn.springonetomany.Repository;

import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Enum.AuthProvider;
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

}

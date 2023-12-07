package hcmute.vn.springonetomany;

import hcmute.vn.springonetomany.Entities.Role;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Repository.IRoleRepository;
import hcmute.vn.springonetomany.Repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private IUserRepository userRepo;
	
	@Autowired
	private IRoleRepository roleRepo;
	
	@Test
	public void testCreateUser() {
		User user = new User();
		user.setEmail("ravikumar@gmail.com");
		user.setPassword("ravi2020");
		user.setFirstName("Ravi");
		user.setLastName("Kumar");
		
		User savedUser = userRepo.save(user);
		
		User existUser = entityManager.find(User.class, savedUser.getId());
		assertThat(user.getEmail()).isEqualTo(existUser.getEmail());
	}
	
	@Test
	public void testAddRoleToNewUser() {
		Role roleAdmin = roleRepo.findByName("Admin");
		
		User user = new User();
		user.setEmail("mikes.gates@gmail.com");
		user.setPassword("mike2020");
		user.setFirstName("Mike");
		user.setLastName("Gates");
		user.addRole(roleAdmin);		
		
		User savedUser = userRepo.save(user);
		
	}
	
	@Test
	public void testAddRoleToExistingUser() {
		User user = userRepo.findById(1L).get();
		Role roleUser = roleRepo.findByName("User");
		Role roleCustomer = new Role(3);
		
		user.addRole(roleUser);
		user.addRole(roleCustomer);
		
		User savedUser = userRepo.save(user);
		
	}
	
	@Test
	public void testFindByEmail() {
		String email = "21110362@student.hcmute.edu.vn";
		User user = userRepo.findByEmail(email);
		assertThat(user.getEmail()).isEqualTo(email);
	}
}

package hcmute.vn.springonetomany.Entities;

import hcmute.vn.springonetomany.Enum.AuthProvider;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Email(message = "Vui lòng nhập đúng định dạng Email")
	@Column(nullable = false, unique = true, length = 45)
	private String email;

	@Column(nullable = true, length = 64)
	private String password;
	
	@NotNull(message = "Không được bỏ trống")
	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;
	
	@NotNull(message = "Không dược bỏ trống")
	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private WishList wishList;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	
	private Set<Role> roles = new HashSet<>();

	@ManyToMany(mappedBy = "users")
    private Set<Voucher> vouchers = new HashSet<>();
	
    @Column(name = "birth_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @Nationalized
    @Column(name = "country", length = 50)
    private String country;

    @Nationalized
    @Column(name = "gender", length = 5)
    private String gender;

	@Size(min = 10, max = 12, message = "Số điện thoại phải từ 10 đến 12 kí tự")
    @Column(name = "phone", length = 15)
    private String phone;

    @Nationalized
	@Lob
    @Column(name = "photos")
    private String photos;

	@Enumerated(EnumType.STRING)
	@Column(name = "auth_provider")
	private AuthProvider authProvider;

	@CreationTimestamp
    private Date createdAt;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Cart cart;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Order> order = new LinkedHashSet<>();

	@OneToMany(mappedBy = "user")
    private Set<Rating> ratings = new LinkedHashSet<>();
	
	public void addRole(Role role) {
		this.roles.add(role);
	}

	@Transient
	public String getPhotosImagePath() {
		if (photos == null || id == null) return null;

		return "/user_photos/" + id + "/" + photos;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
}

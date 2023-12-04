package hcmute.vn.springonetomany.Entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
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

	@Column(nullable = false, length = 64)
	private String password;
	
	@NotBlank(message = "Không được bỏ trống")
	@Column(name = "first_name", nullable = false, length = 20)
	private String firstName;
	
	@NotBlank(message = "Không dược bỏ trống")
	@Column(name = "last_name", nullable = false, length = 20)
	private String lastName;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_vouchers",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "voucher_id")
	)
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

    @Nationalized
    @Column(name = "google_auth", length = 50)
    private String googleAuth;

	@Size(min = 4, max = 12, message = "Số điện thoại phải từ 4 đến 12 kí tự")
    @Column(name = "phone", length = 15)
    private String phone;

    @Nationalized
    @Column(name = "photos", length = 100)
    private String photos;

	@CreationTimestamp
    private Date createdAt;

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
}

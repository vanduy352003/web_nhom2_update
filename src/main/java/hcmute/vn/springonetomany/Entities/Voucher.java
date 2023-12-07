package hcmute.vn.springonetomany.Entities;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vouchers")
public class Voucher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    @PositiveOrZero(message = "Số lượng voucher phải lớn hơn 0")
	private Integer amount;
    @NotBlank(message = "Tên voucher không được bỏ trống")
    @Nationalized
	private String name;
	@CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "from_date")
	private Date fromDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "to_date")
    private Date toDate;
	private String status;
	private String type;
    @Positive(message = "Giá trị voucher phải lớn hơn không")
	private Integer value;
	@Nationalized
    @Column(name = "photos", length = 64)
    private String photos;
	
	@Transient
    public String getPhotosImagePath() {
        if (photos == null || id == null) return null;

        return "/voucher_photos/" + id + "/" + photos;
    }
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "users_vouchers",
			joinColumns = @JoinColumn(name = "voucher_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private Set<User> users = new HashSet<>();
	
	
	public void removeUser(User user) {
		this.users.remove(user);
		user.getVouchers().remove(this);
	}
	
}

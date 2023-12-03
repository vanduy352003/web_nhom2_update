package hcmute.vn.springonetomany.Entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vouchers")
public class Voucher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer amount;
	private String name;
	@CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "expired_date")
	private Date expiredDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "from_date")
	private Date fromDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "to_date")
    private Date toDate;
	private String status;
	private String type;
	private Integer value;
	@Nationalized
    @Column(name = "photos", length = 64)
    private String photos;
	
	@Transient
    public String getPhotosImagePath() {
        if (photos == null || id == null) return null;

        return "/voucher_photos/" + id + "/" + photos;
    }
	
	
}

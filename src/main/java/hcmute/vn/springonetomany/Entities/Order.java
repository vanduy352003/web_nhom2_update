package hcmute.vn.springonetomany.Entities;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
	/*
	 * @Id
	 * 
	 * @GeneratedValue(strategy = GenerationType.IDENTITY)
	 * 
	 * @Column(name = "id", nullable = false) private Integer id;
	 * 
	 * @Column(name = "create_at")
	 * 
	 * @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate create_at;
	 * 
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "user_id") private User user;
	 * 
	 * @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch =
	 * FetchType.EAGER) private Set<OrderLines> orderLines = new LinkedHashSet<>();
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderLines> orderLines;

	@Column(name = "create_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate create_at;

}

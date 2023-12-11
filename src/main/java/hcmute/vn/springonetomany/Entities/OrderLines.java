package hcmute.vn.springonetomany.Entities;

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
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_lines")
public class OrderLines {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "id", nullable = false)
//	private Integer id;
//
//	@OneToMany(fetch = FetchType.EAGER)
//	@JoinColumn(name = "order_id", referencedColumnName = "id")
//	private Order orderId;
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @ManyToOne
	    @JoinColumn(name = "order_id")
	    private Order order;
	    
	    @ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Product product;

    @Column(name = "price")
    private Double price;
    
	@Column(name = "quantity")
	private Integer quantity;
//	private List<OrderLines> orderLines;
//	public void setOrderLines(List<OrderLines> orderLines) {
//	    this.orderLines = orderLines;
//	}
	
}

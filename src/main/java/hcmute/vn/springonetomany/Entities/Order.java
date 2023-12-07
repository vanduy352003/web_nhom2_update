package hcmute.vn.springonetomany.Entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
//@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "create_at")
	private String create_at;

	@Column(name = "name")
	private String name;

	@Column(name = "status_payment")
	private String status_payment;

	@Column(name = "total")
	private String total;

	@Column(name = "user_address")
	private String user_address;

	@Column(name = "payment_type_id")
	private String payment_type_id;

	@Column(name = "voucher_order")
	private String voucher_order;

	@Column(name = "user_id")
	private String user_id;

	@Column(name = "ship_id")
	private String ship_id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreate_at() {
		return create_at;
	}

	public void setCreate_at(String create_at) {
		this.create_at = create_at;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus_payment() {
		return status_payment;
	}

	public void setStatus_payment(String status_payment) {
		this.status_payment = status_payment;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getUser_address() {
		return user_address;
	}

	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}

	public String getPayment_type_id() {
		return payment_type_id;
	}

	public void setPayment_type_id(String payment_type_id) {
		this.payment_type_id = payment_type_id;
	}

	public String getVoucher_order() {
		return voucher_order;
	}

	public void setVoucher_order(String voucher_order) {
		this.voucher_order = voucher_order;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getShip_id() {
		return ship_id;
	}

	public void setShip_id(String ship_id) {
		this.ship_id = ship_id;
	}

	public Order(Integer id, String create_at, String name, String status_payment, String total, String user_address,
			String payment_type_id, String voucher_order, String user_id, String ship_id) {
		super();
		this.id = id;
		this.create_at = create_at;
		this.name = name;
		this.status_payment = status_payment;
		this.total = total;
		this.user_address = user_address;
		this.payment_type_id = payment_type_id;
		this.voucher_order = voucher_order;
		this.user_id = user_id;
		this.ship_id = ship_id;
	}

}

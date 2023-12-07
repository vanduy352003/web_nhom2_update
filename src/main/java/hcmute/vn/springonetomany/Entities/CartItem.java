package hcmute.vn.springonetomany.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.NumberFormat;
import java.util.Locale;

@Getter
@Setter
@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @Transient
    private Double total;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public String getPriceFormatted() {
        if (id == null || this.product.getPrice() == null) return null;

        Locale locale = new Locale.Builder().setLanguage("vi").setRegion("VN").build();
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
//        format.setGroupingUsed(false);
        String formattedPrice = format.format(this.getTotal());
        return formattedPrice.replace(".", ",");
    }
    public Double getTotal() {
        total = this.product.getPrice() * quantity;
        return total;
    }
    public void updateTotal(int newQuantity) {
        total = this.product.getPrice() * newQuantity;
    }
}
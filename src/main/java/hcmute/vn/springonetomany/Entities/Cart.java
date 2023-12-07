package hcmute.vn.springonetomany.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.text.NumberFormat;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "total")
    private Double total = (double) 0;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    public String getPriceFormatted() {
        if (id == null) return null;

        Locale locale = new Locale.Builder().setLanguage("vi").setRegion("VN").build();
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
//        format.setGroupingUsed(false);
        String formattedPrice = format.format(this.getTotal());
        return formattedPrice.replace(".", ",");
    }


}
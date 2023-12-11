package hcmute.vn.springonetomany.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.text.NumberFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank(message = "Tên sản phẩm không được bỏ trống")
    @Nationalized
    @Column(name = "name")
    private String name;

    @Positive(message = "Giá sản phẩm phải lớn hơn không")
    @Column(name = "price")
    private Double price;

    @Nationalized
    @Column(name = "photos", length = 64)
    private String photos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Lob
    @Nationalized
    @Column(name = "description")
    private String description;

    @PositiveOrZero(message = "Số lượng bán phải lớn hơn 0")
    @Column(name = "sell_amount")
    private Integer sellAmount = 0;

    @PositiveOrZero(message = "Số lượng tồn kho không là số âm")
    @Column(name = "inventory")
    private Integer inventory;

    @CreationTimestamp
    private Date createdAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderLines> orderLines = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<Rating> ratings;

    @Transient
    public String getPhotosImagePath() {
        if (photos == null || id == null) return null;

        return "/product_photos/" + id + "/" + photos;
    }

    public Integer getRatingPoint() {
    	if (ratings.isEmpty())
    		return 0;
    	Integer totalRatingPoints = 0;
    	for (Rating rating : ratings) {
    		totalRatingPoints += rating.getRatingPoint();
    	}
    	return Math.round((float)totalRatingPoints/ratings.size());
    }

    public Product(Integer id, String name, Double price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getPriceFormatted() {
        if (id == null || price == null) return null;

        Locale locale = new Locale.Builder().setLanguage("vi").setRegion("VN").build();
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
//        format.setGroupingUsed(false);
        String formattedPrice = format.format(this.price);
        return formattedPrice.replace(".", ",");
    }
    @OneToMany(mappedBy = "product")
    private Set<ProductImages> productImages = new LinkedHashSet<>();
}
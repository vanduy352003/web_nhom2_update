package hcmute.vn.springonetomany.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

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

    @Nationalized
    @Column(name = "name")
    private String name;

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

    @Column(name = "sell_amount")
    private Integer sellAmount = 0;

    @Column(name = "inventory")
    private Integer inventory;

    @CreationTimestamp
    private Date createdAt;

    @Transient
    public String getPhotosImagePath() {
        if (photos == null || id == null) return null;

        return "/product_photos/" + id + "/" + photos;
    }

    public Product(Integer id, String name, Double price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
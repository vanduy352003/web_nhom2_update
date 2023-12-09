package hcmute.vn.springonetomany.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hcmute.vn.springonetomany.Entities.Order;
import hcmute.vn.springonetomany.Model.ProductReport;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Integer>{
	@Query("SELECT new hcmute.vn.springonetomany.Model.ProductReport(l.product.name, count(*))FROM Order o, OrderLines l WHERE o.create_at = ?1 AND l.order.id=o.id GROUP BY l.product.name")
    List<ProductReport> getAmountProductByDate(LocalDate date);

}

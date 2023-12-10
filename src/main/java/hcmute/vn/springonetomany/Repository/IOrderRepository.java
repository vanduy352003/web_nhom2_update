package hcmute.vn.springonetomany.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hcmute.vn.springonetomany.Entities.Order;
import hcmute.vn.springonetomany.Model.ProductReport;
import hcmute.vn.springonetomany.Model.ProfitReportByDay;
import hcmute.vn.springonetomany.Model.ProfitReportByMonth;
import hcmute.vn.springonetomany.Model.ProfitReportByYear;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Integer> {
	// Thống kê số lướng sản phấm bán được
	@Query("SELECT new hcmute.vn.springonetomany.Model.ProductReport(l.product.name, sum(l.quantity)) FROM Order o, OrderLines l WHERE o.create_at = ?1 AND l.order.id=o.id GROUP BY l.product.name, l.product.id")
	List<ProductReport> getAmountProductByDate(LocalDate date);

	@Query("SELECT new hcmute.vn.springonetomany.Model.ProductReport(l.product.name, sum(l.quantity)) FROM Order o, OrderLines l WHERE YEAR(o.create_at) = ?1 AND MONTH(o.create_at) = ?2 AND l.order.id=o.id GROUP BY l.product.name, l.product.id")
	List<ProductReport> getAmountProductByMonth(int year, int month);

	@Query("SELECT new hcmute.vn.springonetomany.Model.ProductReport(l.product.name, sum(l.quantity)) FROM Order o, OrderLines l WHERE YEAR(o.create_at) = ?1 AND l.order.id=o.id GROUP BY l.product.name, l.product.id")
	List<ProductReport> getAmountProductByYear(int year);

	// Thống kê doanh thu
	@Query("SELECT new hcmute.vn.springonetomany.Model.ProfitReportByDay(l.product.name, o.total) FROM Order o, OrderLines l WHERE o.create_at = ?1 AND l.order.id=o.id GROUP BY l.product.name, o.total")
	List<ProfitReportByDay> getProfitByDate(LocalDate date);

	@Query("SELECT sum(o.total) FROM Order o, OrderLines l WHERE o.create_at = ?1 AND l.order.id=o.id")
	Optional<Integer> findSumProfitByDate(LocalDate date);

	@Query("SELECT new hcmute.vn.springonetomany.Model.ProfitReportByMonth(o.create_at, sum(o.total)) FROM Order o, OrderLines l WHERE YEAR(o.create_at) = ?1 AND MONTH(o.create_at) = ?2 AND l.order.id=o.id GROUP BY o.create_at")
	List<ProfitReportByMonth> getProfitByMonth(int year, int month);

	@Query("SELECT sum(o.total) FROM Order o, OrderLines l WHERE YEAR(o.create_at) = ?1 AND MONTH(o.create_at) = ?2 AND l.order.id=o.id")
	Optional<Integer> findSumProfitByMonth(int year, int month);

	@Query("SELECT new hcmute.vn.springonetomany.Model.ProfitReportByYear(MONTH(o.create_at), sum(o.total)) FROM Order o, OrderLines l WHERE YEAR(o.create_at) = ?1 AND l.order.id=o.id GROUP BY MONTH(o.create_at)")
	List<ProfitReportByYear> getProfitByYear(int year);

	@Query("SELECT sum(o.total) FROM Order o, OrderLines l WHERE YEAR(o.create_at) = ?1 AND l.order.id=o.id")
	Optional<Integer> findSumProfitByYear(int year);

	// -------------------------------------------
}

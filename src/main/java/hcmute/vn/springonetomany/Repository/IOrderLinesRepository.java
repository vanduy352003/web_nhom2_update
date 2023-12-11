package hcmute.vn.springonetomany.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hcmute.vn.springonetomany.Entities.OrderLines;

@Repository
public interface IOrderLinesRepository extends JpaRepository<OrderLines, Integer> {
    // Các phương thức cụ thể có thể được thêm vào đây nếu cần
}

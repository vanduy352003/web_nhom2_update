package hcmute.vn.springonetomany.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hcmute.vn.springonetomany.Entities.Rating;

public interface IRatingRepository extends JpaRepository<Rating, Integer>{
	 @Query("select r from Rating r where r.product.id = ?1")
	 Page<Rating> getRatingsByProduct_Id(int product_id, Pageable pageable);
}

package hcmute.vn.springonetomany.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hcmute.vn.springonetomany.Entities.RatingImage;

@Repository
public interface IRatingImageRepository extends JpaRepository<RatingImage, Integer>{
//	@Query("delete from RatingImage r where r.rating.id = ?1")
//	void deleteByRating_Id(int ratingId);
}

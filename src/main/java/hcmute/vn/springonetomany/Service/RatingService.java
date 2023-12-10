package hcmute.vn.springonetomany.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hcmute.vn.springonetomany.Entities.Rating;
import hcmute.vn.springonetomany.Repository.IRatingRepository;

@Service
public class RatingService {
	int PAGE_SIZE = 2;
	@Autowired
	private IRatingRepository ratingRepository;

	public <S extends Rating> S save(S entity) {
		return ratingRepository.save(entity);
	}

	public List<Rating> findAll() {
		return ratingRepository.findAll();
	}

	public Page<Rating> findAll(Pageable pageable) {
		return ratingRepository.findAll(pageable);
	}

	public Rating findById(Integer id) {
		return ratingRepository.findById(id).get();
	}

	public long count() {
		return ratingRepository.count();
	}

	public void deleteById(Integer id) {
		ratingRepository.deleteById(id);
	}

	public void delete(Rating entity) {
		ratingRepository.delete(entity);
	}
	
	public Page<Rating> getRatingsByProduct_Id(int product_id, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);
        return ratingRepository.getRatingsByProduct_Id(product_id, pageable);
    }
	
	public Rating getNewRating(Rating rating) {
        return ratingRepository.save(rating);
    }
	
	
}

package hcmute.vn.springonetomany.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hcmute.vn.springonetomany.Entities.RatingImage;
import hcmute.vn.springonetomany.Repository.IRatingImageRepository;

@Service
public class RatingImageService {
	
	@Autowired
	IRatingImageRepository ratingImageRepository;
	
	public RatingImage getNewRatingImage(RatingImage ratingImage) {
        return ratingImageRepository.save(ratingImage);
    }

	public void deleteById(Integer id) {
		ratingImageRepository.deleteById(id);
	}

	public void delete(RatingImage entity) {
		ratingImageRepository.delete(entity);
	}
	
//	public void deleteByRating_Id(int ratingId) {
//		ratingImageRepository.deleteByRating_Id(ratingId);
//	}
}

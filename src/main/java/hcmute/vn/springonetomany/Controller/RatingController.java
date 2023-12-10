package hcmute.vn.springonetomany.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import hcmute.vn.springonetomany.Custom.CustomUser;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.Rating;
import hcmute.vn.springonetomany.Entities.RatingImage;
import hcmute.vn.springonetomany.Service.ProductService;
import hcmute.vn.springonetomany.Service.RatingImageService;
import hcmute.vn.springonetomany.Service.RatingService;
import hcmute.vn.springonetomany.Service.UserService;
import hcmute.vn.springonetomany.Ultis.FileUploadUtil;

@Controller
public class RatingController {
	@Autowired
	ProductService productService;
	@Autowired
	UserService userService;
	@Autowired
	RatingService ratingService;
	@Autowired
	RatingImageService ratingImageService;
	
	
	@PostMapping("rating/send/{id}")
	public String sendRating(@AuthenticationPrincipal CustomUser loggedUser,@Valid Rating rating,
			@RequestParam(value = "ratingImage") List<MultipartFile> ratingImages,
			@PathVariable("id") int id) throws Exception {
		if (rating.getRatingPoint() == null) {
			rating.setRatingPoint(0);
		}
		Rating newRating = new Rating();
		Product product = productService.findById(id);
		newRating.setUser(userService.findUserByEmail(loggedUser.getEmail()));
		newRating.setProduct(product);
		newRating.setDate(rating.getDate());
		newRating.setMessage(rating.getMessage());
		newRating.setRatingPoint(rating.getRatingPoint());
		
		Rating savedRating = ratingService.getNewRating(newRating);
		
		if (!(ratingImages == null || ratingImages.isEmpty() || ratingImages.stream().allMatch(MultipartFile::isEmpty))) {
			for (MultipartFile multipartFile : ratingImages) {
				String filename = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
				RatingImage ratingImage = new RatingImage();
				ratingImage.setImageUrl(filename);
				ratingImage.setRating(savedRating);
				RatingImage savedRatingImage = ratingImageService.getNewRatingImage(ratingImage);
				String uploadDir = "rating_images/" + savedRating.getId() + "/" + savedRatingImage.getId();
	            FileUploadUtil.saveFile(uploadDir, filename, multipartFile);
	            rating.getRatingImages().add(ratingImage);
			}
		}
		return "redirect:/products/detail/" + id;
	}
	
	@GetMapping("rating/delete") 
	public String deleteRating(@RequestParam("id") int id, @RequestParam("productId") int productId) throws IOException {
		Rating rating = ratingService.findById(id);
		for (RatingImage ratingImage : rating.getRatingImages()) {
    		ratingImageService.delete(ratingImage);
    	}
        FileUploadUtil.deleteAllFiles("rating_images/" + rating.getId());
        rating.getRatingImages().clear();
        ratingService.delete(rating);
		return "redirect:/products/detail/" + productId;
	}

}

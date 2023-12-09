package hcmute.vn.springonetomany.Controller.Admin;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import hcmute.vn.springonetomany.Entities.Rating;
import hcmute.vn.springonetomany.Entities.RatingImage;
import hcmute.vn.springonetomany.Service.RatingImageService;
import hcmute.vn.springonetomany.Service.RatingService;
import hcmute.vn.springonetomany.Ultis.FileUploadUtil;

@Controller
@RequestMapping("/admin/ratings")
public class AdminRatingController {
	@Autowired
	RatingService ratingService;
	@Autowired
	RatingImageService ratingImageService;
	
	@GetMapping("")
	public String showRatingList(Model model, @RequestParam(required = false, defaultValue = "1") int page) {
        Integer PAGE_SIZE = 2;
		Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
		Page<Rating> listRating = ratingService.findAll(pageable);
        int totalPages = listRating.getTotalPages();
        long totalItems = listRating.getTotalElements();

        model.addAttribute("listrating", listRating);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
		return "rating/admin_ratings";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteRating(@PathVariable("id") int id) throws IOException {
		Rating rating = ratingService.findById(id);
		for (RatingImage ratingImage : rating.getRatingImages()) {
    		ratingImageService.delete(ratingImage);
    	}
 		FileUploadUtil.deleteAllFiles("rating_images/" + rating.getId());
        rating.getRatingImages().clear();
		ratingService.delete(rating);
		return "redirect:/admin/ratings";
	}
	
	@GetMapping("/edit/{id}") 
	public String showEditRatingForm(@PathVariable("id") int id, Model model) {
		Rating rating = ratingService.findById(id);
		model.addAttribute("rating", rating);
		return "rating/rating_form";
	}
	
	@PostMapping("/save")
    private String saveRating(@Valid Rating rating,
                               BindingResult result,
                               @RequestParam(value = "ratingImage") List<MultipartFile> ratingImages,
                               @RequestParam(value = "id", required = false) Integer id,
                               @RequestParam(value = "isXoaAnh", defaultValue="false") boolean isChecked) throws Exception {
        if (result.hasErrors()) {
            return "rating/rating_form";
        }

        Rating oldRating = ratingService.findById(id);
        oldRating.setMessage(rating.getMessage());
        oldRating.setRatingPoint(rating.getRatingPoint());
        ratingService.save(oldRating);
        
        if (isChecked) {
        	for (RatingImage ratingImage : oldRating.getRatingImages()) {
        		ratingImageService.delete(ratingImage);
        	}
            FileUploadUtil.deleteAllFiles("rating_images/" + oldRating.getId());
            oldRating.getRatingImages().clear();
        }
        	
        
        if (!(ratingImages == null || ratingImages.isEmpty() || ratingImages.stream().allMatch(MultipartFile::isEmpty))) {
        	for (RatingImage ratingImage : oldRating.getRatingImages()) {
        		ratingImageService.delete(ratingImage);
        	}
            oldRating.getRatingImages().clear();
            String uploadDir = "rating_images/" + oldRating.getId();
            FileUploadUtil.deleteAllFiles(uploadDir);
			for (MultipartFile multipartFile : ratingImages) {
				String filename = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
				RatingImage ratingImage = new RatingImage();
				ratingImage.setImageUrl(filename);
				ratingImage.setRating(oldRating);
				RatingImage savedRatingImage = ratingImageService.getNewRatingImage(ratingImage);
				String uploadDirs = uploadDir;
				uploadDirs +=  "/" + savedRatingImage.getId();
	            FileUploadUtil.saveFile(uploadDirs, filename, multipartFile);
	            rating.getRatingImages().add(ratingImage);
			}
		}
        
        return "redirect:/admin/ratings";
    }
}

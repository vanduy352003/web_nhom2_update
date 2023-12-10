package hcmute.vn.springonetomany.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hcmute.vn.springonetomany.Entities.ProductImages;
import hcmute.vn.springonetomany.Repository.IProductImagesRepository;

@Service
public class ProductImagesService {
	@Autowired
	IProductImagesRepository productImagesRepository;
	
	public ProductImages getNewProductImages(ProductImages productImages)
	{
		return productImagesRepository.save(productImages);
	}
	
	public void deleteById(Integer id) {
		productImagesRepository.deleteById(id);
	}
	public void delete(ProductImages entity) {
		productImagesRepository.delete(entity);
	}
	
}

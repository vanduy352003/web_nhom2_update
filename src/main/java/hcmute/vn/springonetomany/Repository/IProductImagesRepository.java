package hcmute.vn.springonetomany.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hcmute.vn.springonetomany.Entities.ProductImages;


public interface IProductImagesRepository  extends JpaRepository<ProductImages,Integer>{
	
}

package hcmute.vn.springonetomany.Service;

import hcmute.vn.springonetomany.Entities.Category;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
	int PAGE_SIZE = 2;
	
    @Autowired
    ICategoryRepository repository;

    public List<Category> listAll() {
        return repository.findAll();
    }
    
    public Page<Category> findPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);
        return repository.findAll(pageable);
    }

    public void save(Category category) {
        repository.save(category);
    }
    
    public void deleteById(int id) {
    	repository.deleteById(id);
    }
    
    public Category getNewCategory(Category category) {
        return repository.save(category);
    }

    public Category findById(int id) throws Exception {
        Optional<Category> result = repository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new Exception("Could not find category");
    }
    public int countProduct(int id) throws Exception {
        Optional<Category> result = repository.findById(id);
        if (result.isPresent()) {
            return result.get().getProducts().toArray().length;
        }
        throw new Exception("Could not find category");
    }
    public List<Category> getCategoryExceptsThisCategory(Category category)
    {
    	return repository.getCategoryExceptsThisCategory(category);
    }

	public Optional<Category> getCategory(Integer id) {
		return repository.findById(id);
	}
}

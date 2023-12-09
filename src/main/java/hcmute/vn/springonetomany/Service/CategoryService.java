package hcmute.vn.springonetomany.Service;

import hcmute.vn.springonetomany.Entities.Category;
import hcmute.vn.springonetomany.Repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    ICategoryRepository repository;

    public List<Category> listAll() {
        return repository.findAll();
    }

    public void save(Category category) {
        repository.save(category);
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
}

package hcmute.vn.springonetomany;

import hcmute.vn.springonetomany.Entities.Category;
import hcmute.vn.springonetomany.Repository.ICategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTest {
    @Autowired
    private ICategoryRepository repository;

    @Test
    public void testCreateCategory() {
//        Optional<Category> cate = repository.findById(1);
//        Category cate1 = repository.save(new Category("test"));
//        Optional<Category> cate = repository.findById(1);
//        System.out.println(cate);
    }

}

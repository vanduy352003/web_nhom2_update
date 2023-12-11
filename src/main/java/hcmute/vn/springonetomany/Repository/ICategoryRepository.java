package hcmute.vn.springonetomany.Repository;

import hcmute.vn.springonetomany.Entities.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Integer> {
	@Query("select c from Category c where c.id not like ?1")
	public List<Category> getCategoryExceptsThisCategory(Category category);
}

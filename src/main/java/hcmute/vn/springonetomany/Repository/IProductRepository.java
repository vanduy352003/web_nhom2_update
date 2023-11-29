package hcmute.vn.springonetomany.Repository;

import hcmute.vn.springonetomany.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer>, PagingAndSortingRepository<Product, Integer> {
//    @Query(value = "select p from Product p where p.name like %?1% or p.category.name like %?1%")
//    List<Product> findByKeyWord(@Param("keyword") String keyword);

//    @Query(value = "select p from Product p where p.name like %?1% or p.category.name like %?1%")
    @Query("select p from Product p where p.name like concat('%', ?1, '%') or p.category.name like concat('%', ?1, '%')")
    Page<Product> getProductsByKeyword (String keyword,
                                        Pageable pageable);
    @Query("select p from Product p where p.category.id = ?1")
    Page<Product> getProductsByCategory_Id(int category_id, Pageable pageable);

}

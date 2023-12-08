package hcmute.vn.springonetomany.Repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hcmute.vn.springonetomany.Entities.OrderLines;

@Repository
public interface IOrderLinesRepository extends JpaRepository<OrderLines, Integer>{

}

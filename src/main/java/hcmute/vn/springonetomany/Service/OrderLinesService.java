package hcmute.vn.springonetomany.Service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import hcmute.vn.springonetomany.Entities.OrderLines;
import hcmute.vn.springonetomany.Repository.IOrderLinesRepository;

@Service
public class OrderLinesService {
	@Autowired
	IOrderLinesRepository orderlinesRepository;

	public <S extends OrderLines> S save(S entity) {
		return orderlinesRepository.save(entity);
	}

	public List<OrderLines> findAll() {
		return orderlinesRepository.findAll();
	}

	public <S extends OrderLines> long count(Example<S> example) {
		return orderlinesRepository.count(example);
	}

	public long count() {
		return orderlinesRepository.count();
	}

	public void delete(OrderLines entity) {
		orderlinesRepository.delete(entity);
	}

	public OrderLines getById(Integer id) {
		return orderlinesRepository.getById(id);
	}
	

}

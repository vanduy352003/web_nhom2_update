package hcmute.vn.springonetomany.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hcmute.vn.springonetomany.Entities.Order;
import hcmute.vn.springonetomany.Model.ProductReport;
import hcmute.vn.springonetomany.Repository.IOrderRepository;

import java.text.SimpleDateFormat;
@Service
public class OrderService {
    @Autowired
    IOrderRepository orderRepository;
	public <S extends Order> S save(S entity) {
		return orderRepository.save(entity);
	}
	public List<Order> findAll() {
		return orderRepository.findAll();
	}
	public Optional<Order> findById(Integer id) {
		return orderRepository.findById(id);
	}
	public <S extends Order> Page<S> findAll(Example<S> example, Pageable pageable) {
		return orderRepository.findAll(example, pageable);
	}
	public <S extends Order> long count(Example<S> example) {
		return orderRepository.count(example);
	}
	public long count() {
		return orderRepository.count();
	}
	public void delete(Order entity) {
		orderRepository.delete(entity);
	}
	public Order getById(Integer id) {
		return orderRepository.getById(id);
	}
    
	public List<ProductReport> getAmountProductByDate(LocalDate date) {
		return orderRepository.getAmountProductByDate(date);
	}
	
	public List<ProductReport> getAmountProductByMonth(int year, int month){
		return orderRepository.getAmountProductByMonth(year,month);
	}
	
	public List<ProductReport> getAmountProductByYear(int year){
		return orderRepository.getAmountProductByYear(year);
	} 
    

}

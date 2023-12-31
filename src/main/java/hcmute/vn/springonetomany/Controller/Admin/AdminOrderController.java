package hcmute.vn.springonetomany.Controller.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hcmute.vn.springonetomany.Entities.Order;
import hcmute.vn.springonetomany.Entities.Orders;
import hcmute.vn.springonetomany.Service.OrderService;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {
	@Autowired
	private OrderService orderService;
	
	@GetMapping("")
	public String showOrders(Model model) {
		List<Order> listOrder = orderService.findAll();
		model.addAttribute("listOrder", listOrder);
		return "order/admin_orders";
	}
	
	
}

package hcmute.vn.springonetomany.Controller.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hcmute.vn.springonetomany.Entities.Order;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Repository.IOrderRepository;
import hcmute.vn.springonetomany.Service.CategoryService;
import hcmute.vn.springonetomany.Service.OrderService;
import hcmute.vn.springonetomany.Service.ProductService;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {
    @Autowired
    private IOrderRepository iorderServicIOrderRepository;
	  @GetMapping("")
	    public String showInvoices(Model model) {
		//  List<Order> order = iorderServicIOrderRepository.findAll();
	    //    model.addAttribute("order", order);
	     //   return "order/admin_order";
		  model.addAttribute("orders", iorderServicIOrderRepository.findAll());
	        return "order/admin_order";

	    }
}

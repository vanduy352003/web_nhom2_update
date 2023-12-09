package hcmute.vn.springonetomany.Controller.Admin;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hcmute.vn.springonetomany.Model.ProductReport;
import hcmute.vn.springonetomany.Service.OrderService;

@Controller
@RequestMapping("/admin/report")
public class AdminReportController {
	@Autowired
	private OrderService orderService;

	@GetMapping("")
	public String reportProduct(Model model) {
//      List<Product> listProduct = productService.findAll();
		LocalDate date = LocalDate.now();
		List<ProductReport> listProductReport = orderService.getAmountProductByDate(date);
		model.addAttribute("listProductReport", listProductReport);
		
		return "report/listReport";
	}

}

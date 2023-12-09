package hcmute.vn.springonetomany.Controller.Admin;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hcmute.vn.springonetomany.Model.ProductReport;
import hcmute.vn.springonetomany.Model.ProfitReportByDay;
import hcmute.vn.springonetomany.Model.ProfitReportByMonth;
import hcmute.vn.springonetomany.Model.ProfitReportByYear;
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

	@PostMapping("")
	public String listProductByDate(Model model,
			@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam(name = "type") String type,
			@RequestParam(name = "month") String monthString,
			@RequestParam(name = "year", required = false) String yearString,
			@RequestParam(name = "reportCategory") String reportCategory) {
		System.out.println(type);
		System.out.println("Month: " + monthString);
		if (type == null) {
			date = LocalDate.now();
			List<ProductReport> listProductReport = orderService.getAmountProductByDate(date);
			model.addAttribute("listProductReport", listProductReport);
		}
		if (type.contains("Daily")) {
			List<ProductReport> listProductReport = orderService.getAmountProductByDate(date);
			model.addAttribute("listProductReport", listProductReport);
		} else {
			if (type.contains("Monthly")) {
				YearMonth yearMonth = YearMonth.parse(monthString);
				int yearFromYearMonth = yearMonth.getYear();
				int month = yearMonth.getMonthValue();
				List<ProductReport> listProductReport = orderService.getAmountProductByMonth(yearFromYearMonth,month);
				model.addAttribute("listProductReport", listProductReport);
			} else {
				if (type.contains("Yearly")) {
					int year = Integer.parseInt(yearString);
					List<ProductReport> listProductReport = orderService.getAmountProductByYear(year);
					model.addAttribute("listProductReport", listProductReport);
				}
			}
		}
		if (reportCategory.contains("Profit")) {
			Integer sumProfit = 0;
			if (type.contains("Daily")) {
				List<ProfitReportByDay> listProfitReportByDate = orderService.getProfitByDate(date);
				model.addAttribute("listProfitReport", listProfitReportByDate);
				Optional<Integer> findSumProfitByDate = orderService.findSumProfitByDate(date);
				if (findSumProfitByDate.isPresent()) {
					sumProfit = findSumProfitByDate.get();
					model.addAttribute("sumProfit", sumProfit);
				}
			} else {
				if (type.contains("Monthly")) {
					YearMonth yearMonth = YearMonth.parse(monthString);
					int yearFromYearMonth = yearMonth.getYear();
					int month = yearMonth.getMonthValue();
					List<ProfitReportByMonth> listProfitReportByMonth = orderService.getProfitByMonth(yearFromYearMonth,month);
					model.addAttribute("listProfitReport", listProfitReportByMonth);
					Optional<Integer> findSumProfitByMonth = orderService.findSumProfitByMonth(yearFromYearMonth,month);
					if (findSumProfitByMonth.isPresent()) {
						sumProfit = findSumProfitByMonth.get();
						model.addAttribute("sumProfit", sumProfit);
					}
				} else {
					if (type.contains("Yearly")) {
						int year = Integer.parseInt(yearString);
						List<ProfitReportByYear> listProfitReportByYear = orderService.getProfitByYear(year);
						model.addAttribute("listProfitReport", listProfitReportByYear);
						Optional<Integer> findSumProfitByYear = orderService.findSumProfitByYear(year);
						if (findSumProfitByYear.isPresent()) {
							sumProfit = findSumProfitByYear.get();
							model.addAttribute("sumProfit", sumProfit);
						}
					}
				}
			}
		}
		model.addAttribute("reportCategory", reportCategory);
		model.addAttribute("type", type);

		return "report/listReport";
	}

}

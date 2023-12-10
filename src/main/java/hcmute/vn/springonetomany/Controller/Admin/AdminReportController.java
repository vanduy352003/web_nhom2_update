package hcmute.vn.springonetomany.Controller.Admin;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
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
			@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dateOptional,
			@RequestParam(name = "type", required = false) Optional<String> typeOptional,
			@RequestParam(name = "month", required = false) Optional<String> monthStringOptional,
			@RequestParam(name = "year", required = false) Optional<String> yearStringOptional,
			@RequestParam(name = "reportCategory", required = false) Optional<String> reportCategoryOptional,
			@RequestParam(name = "page") Optional<Integer> page){
		LocalDate date = dateOptional.orElse(LocalDate.now());
		String type = typeOptional.orElse("Daily");
		String monthString = monthStringOptional.orElse("2023-12"); 
		String yearString = yearStringOptional.orElse("1900");
		String reportCategory = reportCategoryOptional.orElse("Product");
		int count = 0;
		int currentPage = page.orElse(1);
		int pageSize = 6;
		if (monthString == "") {
			monthString = "1900-01";
		}
		if (yearString == "") {
			yearString = "1900";
		}
		if (date == null) {
			date = LocalDate.now();
		}
		System.out.println(type);
		System.out.println(reportCategory);
		System.out.println("Month: " + monthString);

		
		if (reportCategory.contains("Product")) {
			if (type.contains("Daily")) {
				List<ProductReport> listProductReport = orderService.getAmountProductByDate(date);
				count = listProductReport.size();
				model.addAttribute("listProductReport", listProductReport);
			} else {
				if (type.contains("Monthly")) {
					YearMonth yearMonth = YearMonth.parse(monthString,DateTimeFormatter.ofPattern("yyyy-MM"));
					int yearFromYearMonth = yearMonth.getYear();
					int month = yearMonth.getMonthValue();
					List<ProductReport> listProductReport = orderService.getAmountProductByMonth(yearFromYearMonth,
							month);
					count = listProductReport.size();
					model.addAttribute("listProductReport", listProductReport);
				} else {
					if (type.contains("Yearly")) {
						int year = Integer.parseInt(yearString);
						List<ProductReport> listProductReport = orderService.getAmountProductByYear(year);
						count = listProductReport.size();
						model.addAttribute("listProductReport", listProductReport);
					}
				}
			}
		}
		if (reportCategory.contains("Profit")) {
			Integer sumProfit = 0;
			if (type.contains("Daily")) {
				List<ProfitReportByDay> listProfitReportByDate = orderService.getProfitByDate(date);
				count = listProfitReportByDate.size();
				model.addAttribute("listProfitReport", listProfitReportByDate);
				Optional<Integer> findSumProfitByDate = orderService.findSumProfitByDate(date);
				if (findSumProfitByDate.isPresent()) {
					sumProfit = findSumProfitByDate.get();
					model.addAttribute("sumProfit", sumProfit);
				}
			} else {
				if (type.contains("Monthly")) {
					YearMonth yearMonth = YearMonth.parse(monthString,DateTimeFormatter.ofPattern("yyyy-MM"));
					int yearFromYearMonth = yearMonth.getYear();
					int month = yearMonth.getMonthValue();
					List<ProfitReportByMonth> listProfitReportByMonth = orderService.getProfitByMonth(yearFromYearMonth,
							month);
					count = listProfitReportByMonth.size();
					model.addAttribute("listProfitReport", listProfitReportByMonth);
					Optional<Integer> findSumProfitByMonth = orderService.findSumProfitByMonth(yearFromYearMonth,
							month);
					if (findSumProfitByMonth.isPresent()) {
						sumProfit = findSumProfitByMonth.get();
						model.addAttribute("sumProfit", sumProfit);
					}
				} else {
					if (type.contains("Yearly")) {
						int year = Integer.parseInt(yearString);						
						List<ProfitReportByYear> listProfitReportByYear = orderService.getProfitByYear(year);
						count = listProfitReportByYear.size();
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

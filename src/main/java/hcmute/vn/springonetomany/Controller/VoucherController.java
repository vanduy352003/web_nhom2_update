package hcmute.vn.springonetomany.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import hcmute.vn.springonetomany.Custom.CustomUser;
import hcmute.vn.springonetomany.Custom.CustomUserDetails;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Entities.Voucher;
import hcmute.vn.springonetomany.Service.UserService;
import hcmute.vn.springonetomany.Service.VoucherService;

@Controller
public class VoucherController {
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private UserService userService;
	private int PAGE_SIZE = 3;
	
	@GetMapping("/vouchers")
    public String getOnePage(Model model, @RequestParam(required = false, defaultValue = "1") int page, @AuthenticationPrincipal CustomUser loggedUser) {
		Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
		Page<Voucher> voucherPage;
		if (loggedUser != null)
        	voucherPage = voucherService.getVoucherNotOwed(userService.findUserByEmail(loggedUser.getEmail()),pageable);
		else 
			voucherPage = voucherService.findAll(pageable);
        List<Voucher> voucherList = voucherPage.getContent();
        int totalPages = voucherPage.getTotalPages();
        long totalItems = voucherPage.getTotalElements();
        
        model.addAttribute("voucherList", voucherList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        return "voucher/vouchers";
    }
	
	@GetMapping("/vouchers/redeem/{id}")
	public String reedemVoucher(@AuthenticationPrincipal CustomUser loggedUser, @PathVariable("id") int id, Model model) throws Exception {
		Voucher voucher = voucherService.findById(id);
		String email = loggedUser.getEmail();
		User user = userService.findUserByEmail(email);

		if (voucher.getAmount() > 0) {
			voucher.getUsers().add(user);
			voucher.setAmount(voucher.getAmount()- 1);
			if (voucher.getAmount() == 0)
				voucher.setStatus("0");
			voucherService.save(voucher);
		}
		
		return "redirect:/vouchers";
	}
	
	
}
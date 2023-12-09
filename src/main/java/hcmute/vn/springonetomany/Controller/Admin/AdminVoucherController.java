package hcmute.vn.springonetomany.Controller.Admin;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import hcmute.vn.springonetomany.Entities.Category;
import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.Voucher;
import hcmute.vn.springonetomany.Service.VoucherService;
import hcmute.vn.springonetomany.Ultis.FileUploadUtil;

@Controller
@RequestMapping("/admin/vouchers")
public class AdminVoucherController {
	@Autowired
	private VoucherService voucherService;
	private int PAGE_SIZE = 2;
	@GetMapping("")
	public String showVouchersPage(Model model, @RequestParam(required = false, defaultValue = "1") int page) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
		Page<Voucher> listVoucher = voucherService.findAll(pageable);
		
		int totalPages = listVoucher.getTotalPages();
        long totalItems = listVoucher.getTotalElements();

        model.addAttribute("listvoucher", listVoucher);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
		return "voucher/admin_voucher";
	}

	@GetMapping("/new")
    public String showNewVoucherForm(Model model) {
        model.addAttribute("voucher", new Voucher());
        return "voucher/voucher_form";
    }
	
    @GetMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable("id") int id) {
        voucherService.deleteById(id);
        return "redirect:/admin/vouchers";
    }

    @GetMapping("/edit/{id}")
    public String showEditVoucherForm(@PathVariable("id") int id, Model model) {
        try {
            Voucher voucher = voucherService.findById(id);
            model.addAttribute("voucher", voucher);
            return "voucher/voucher_form";
        } catch (Exception e) {
            return "redirect:/admin/vouchers";
        }
    }

    @PostMapping("/save")
    private String saveVoucher(Voucher voucher, @RequestParam(value = "image") MultipartFile multipartFile, @RequestParam(value = "id", required = false) Integer id) throws Exception {

    	String fileName = id == null || (multipartFile != null && !multipartFile.isEmpty())
                ? StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()))
                : voucherService.findById(id).getPhotos();
    	
    	if (id != null)
    		voucher.setCreatedAt(voucherService.findById(id).getCreatedAt());
        voucher.setPhotos(fileName);
        if (voucher.getAmount() > 0)
        	voucher.setStatus("1");
        else 
        	voucher.setStatus("0");
        Voucher savedVoucher = voucherService.getNewVoucher(voucher);
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String uploadDir = "voucher_photos/" + savedVoucher.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        return "redirect:/admin/vouchers";
    }
}

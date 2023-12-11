package hcmute.vn.springonetomany.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hcmute.vn.springonetomany.Entities.Product;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Entities.Voucher;
import hcmute.vn.springonetomany.Repository.IVoucherRepository;

@Service
public class VoucherService {
	@Autowired
	private IVoucherRepository voucherRepo;

	public VoucherService() {
		super();
	}

	public <S extends Voucher> S save(S entity) {
		return voucherRepo.save(entity);
	}

	public List<Voucher> findAll() {
		return voucherRepo.findAll();
	}

	public Page<Voucher> findAll(Pageable pageable) {
		return voucherRepo.findAll(pageable);
	}


	public void deleteById(Integer id) {
		voucherRepo.deleteById(id);
	}

	public long count() {
		return voucherRepo.count();
	}


	public void delete(Voucher entity) {
		voucherRepo.delete(entity);
	}
	
	public Voucher findById(Integer id) throws Exception {
        Optional<Voucher> result = voucherRepo.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new Exception("Could not find product");
    }
	
	public Voucher getNewVoucher(Voucher voucher) {
        return voucherRepo.save(voucher);
    }
	
	public Page<Voucher> getVoucherNotOwed(User user, Pageable pageable) {
		return voucherRepo.getVouchersNotOwned(user, pageable);
	}
	
//	public List<Voucher> getVoucherOwned(User user) {
//		return voucherRepo.getVouchersOwned(user);
//	}
}
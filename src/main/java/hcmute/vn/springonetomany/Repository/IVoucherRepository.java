package hcmute.vn.springonetomany.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Entities.Voucher;

@Repository
public interface IVoucherRepository extends JpaRepository<Voucher, Integer>{

	@Query("select v from Voucher v where ?1 NOT MEMBER OF v.users")
    Page<Voucher> getVouchersNotOwned(User user, Pageable pageable);
	@Query("select v from Voucher v where ?1 MEMBER OF v.users")
    List<Voucher> getVouchersOwned(User user);

}
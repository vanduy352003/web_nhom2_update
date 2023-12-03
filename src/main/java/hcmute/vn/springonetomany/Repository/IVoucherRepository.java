package hcmute.vn.springonetomany.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hcmute.vn.springonetomany.Entities.Voucher;

@Repository
public interface IVoucherRepository extends JpaRepository<Voucher, Integer>{

}

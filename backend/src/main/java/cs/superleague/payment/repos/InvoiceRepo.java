package cs.superleague.payment.repos;

import cs.superleague.payment.dataclass.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, UUID> {
}


package cs.superleague.shopping.repos;

import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CatalogueRepo extends JpaRepository<Catalogue, UUID> {
}


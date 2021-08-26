package cs.superleague.recommendation.repos;

import cs.superleague.recommendation.dataclass.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecommendationRepo extends JpaRepository<Recommendation, UUID> {
}

package covid.fukui.vaccine.ciofeeder.infrastructure.db.repositoryimpl;

import covid.fukui.vaccine.ciofeeder.domain.repository.db.FirestoreRepository;
import covid.fukui.vaccine.ciofeeder.infrastructure.db.dto.VaccinationCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gcp.data.firestore.FirestoreTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * FirestoreのRepository
 */
@Repository
@RequiredArgsConstructor
public class FirestoreRepositoryImpl implements FirestoreRepository {

    private final FirestoreTemplate firestoreTemplate;

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Mono<VaccinationCollection> insertVaccination(
            final VaccinationCollection vaccination) {
        return firestoreTemplate.save(vaccination);
    }
}

package covid.fukui.vaccine.ciofeeder.domain.repository.db;

import covid.fukui.vaccine.ciofeeder.infrastructure.db.dto.VaccinationCollection;
import reactor.core.publisher.Mono;

public interface FirestoreRepository {

    /**
     * firestoreに接種データを保存する
     *
     * @param vaccination firestore形式の接種データ
     * @return 保存された接種データ
     */
    Mono<VaccinationCollection> insertVaccination(VaccinationCollection vaccination);
}

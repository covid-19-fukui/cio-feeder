package covid.fukui.vaccine.ciofeeder.domain.repository.api;

import covid.fukui.vaccine.ciofeeder.infrastructure.api.dto.VaccinationResponse;
import reactor.core.publisher.Flux;

public interface CioRepository {

    /**
     * 都道府県別接種回数詳細
     *
     * @return ワクチンの接種回数(都道府県別)
     */
    Flux<VaccinationResponse> getVaccination();

}

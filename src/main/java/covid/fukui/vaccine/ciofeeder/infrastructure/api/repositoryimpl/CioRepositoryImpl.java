package covid.fukui.vaccine.ciofeeder.infrastructure.api.repositoryimpl;

import covid.fukui.vaccine.ciofeeder.domain.repository.api.CioRepository;
import covid.fukui.vaccine.ciofeeder.infrastructure.api.dto.VaccinationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * cioのRepository実装クラス
 */
@Repository
@RequiredArgsConstructor
public class CioRepositoryImpl implements CioRepository {

    private final WebClient cioApiClient;


    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Flux<VaccinationResponse> getVaccination() {
        return cioApiClient
                .get()
                //.uri(uriBuilder -> uriBuilder.queryParam(ID_PARAM, id).build())
                //.header(HttpHeaders.AUTHORIZATION, oauthHeader)
                .retrieve()
                .bodyToFlux(VaccinationResponse.class);
    }
}

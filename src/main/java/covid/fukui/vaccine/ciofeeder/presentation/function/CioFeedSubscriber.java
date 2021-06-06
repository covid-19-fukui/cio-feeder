package covid.fukui.vaccine.ciofeeder.presentation.function;

import covid.fukui.vaccine.ciofeeder.application.service.VaccinationService;
import covid.fukui.vaccine.ciofeeder.infrastructure.db.dto.VaccinationCollection;
import covid.fukui.vaccine.ciofeeder.presentation.function.dto.PubSubMessage;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CioFeedSubscriber {

    private final VaccinationService vaccinationService;

    /**
     * pubsubでメッセージを受け取る関数
     *
     * @return PubSubMessageを引数に持つ関数
     */
    @Bean
    @NonNull
    public Consumer<PubSubMessage> pubSubFunction() {
        return message -> {
            log.info("更新開始");
            final VaccinationCollection vaccinationList =
                    vaccinationService.saveVaccination().block();

            if (Objects.isNull(vaccinationList)) {
                log.error("接種統計データのfirestoreへの保存が失敗しました");
            }
            log.info("更新終了");
        };
    }
}

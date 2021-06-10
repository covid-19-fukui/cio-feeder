package covid.fukui.vaccine.ciofeeder.config;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Configuration
public class ClockConfig {

    /**
     * クロックのbean取得
     *
     * @return clockのbean
     */
    @Bean
    @NonNull
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    /**
     * 固定されたクロックのbean取得
     *
     * @return clockのbean
     */
    @NonNull
    private Clock getFixedClock() {
        return Clock.fixed(ZonedDateTime.of(
                2021,
                6,
                2,
                12,
                0,
                0,
                0, ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }
}

package covid.fukui.vaccine.ciofeeder.infrastructure.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.util.MimeType;

@Configuration
public class JsonConfiguration {

    @Bean
    public CodecCustomizer ndJsonCustomizer(final ObjectMapper objectMapper) {
        final Jackson2JsonDecoder jsonDecoder = new Jackson2JsonDecoder(
                objectMapper,
                new MimeType("application", "json"),
                new MimeType("application", "x-ndjson"));
        return codecs -> codecs.defaultCodecs().jackson2JsonDecoder(jsonDecoder);
    }

}

package covid.fukui.vaccine.ciofeeder.presentation.function.dto;

import java.io.Serializable;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

/**
 * Google Cloud PubsubからPublishされるメッセージ
 */
@Getter
@ToString
@RequiredArgsConstructor
@Builder
public class PubSubMessage implements Serializable {

    private static final long serialVersionUID = -472187797848029251L;
    /**
     * pub/subのペイロード
     */
    @NonNull
    private final String data;

    /**
     * attributes
     */
    @NonNull
    private final Map<String, String> attributes;

    /**
     * メッセージID
     */
    @NonNull
    private final String messageId;

    /**
     * publishされた時刻
     */
    @NonNull
    private final String publishTime;
}

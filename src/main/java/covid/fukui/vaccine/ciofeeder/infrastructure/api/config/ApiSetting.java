package covid.fukui.vaccine.ciofeeder.infrastructure.api.config;

import java.io.Serializable;
import java.time.Duration;
import lombok.Data;

/**
 * API設定値
 */
@Data
public class ApiSetting implements Serializable {

    private static final long serialVersionUID = 7388502658164579642L;

    /**
     * URL
     */
    private String baseUrl;

    /**
     * 接続タイムアウト
     */
    private Duration connectTimeout;

    /**
     * 読み込みタイムアウト
     */
    private Duration readTimeout;

    /**
     * 最大メモリサイズ
     */
    private Integer maxInMemorySize;
}


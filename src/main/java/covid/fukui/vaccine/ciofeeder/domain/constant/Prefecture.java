package covid.fukui.vaccine.ciofeeder.domain.constant;

import java.util.Arrays;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * 都道府県
 */
@RequiredArgsConstructor
@Getter
public enum Prefecture {
    /**
     * 福井県の都道府県コード
     */
    FUKUI("18", "fukui", 18),
    /**
     * 福井県以外の都道府県コード
     */
    OTHER(null, "other", null);

    /**
     * 都道府県コード値
     */
    private final String prefectureCode;

    private static final Pattern PREFECTURE_CODE_PATTERN = Pattern.compile("^[0-3][0-9]|4[0-7]$");

    /**
     * firestoreのキー値
     */
    private final String firestoreKey;

    /**
     * firestoreの値
     */
    private final Integer firestoreValue;

    /**
     * 都道府県コード値に対応するenumを取得する
     *
     * @param value 都道府県コード値
     * @return enum
     * @throws IllegalArgumentException 都道府県コード値が不正な値の場合の例外
     */
    @NonNull
    public static Prefecture getPrefecture(final String value) throws IllegalArgumentException {

        if (!isPrefecture(value)) {
            // 都道府県コード以外の場合、IllegalArgumentExceptionをthrowする
            throw new IllegalArgumentException();
        }

        // 01-47の都道府県から該当する都道府県のenumを返す
        return Arrays.stream(Prefecture.values())
                // その他を除く(不要なロジックだが、デグレした場合を考慮する)
                .filter(prefecture -> prefecture != Prefecture.OTHER)
                // 都道府県コードが一致するenumのみfilterする
                .filter(prefecture -> prefecture.getPrefectureCode().equals(value))
                // 見つからない場合、その他を返す
                .findFirst().orElse(Prefecture.OTHER);
    }

    /**
     * 福井県の都道府県コード値であるか判定
     *
     * @return 福井県のコードである場合、trueを返す
     */
    public boolean isFukui() {
        return this == FUKUI;
    }

    /**
     * 都道府県コード値であるか判定
     *
     * @param value 都道府県コード値
     * @return 都道府県コードの場合、trueを返す
     */
    public static boolean isPrefecture(final String value) {
        return PREFECTURE_CODE_PATTERN.matcher(value).find();
    }
}

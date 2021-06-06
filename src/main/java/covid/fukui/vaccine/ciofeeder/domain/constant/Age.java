package covid.fukui.vaccine.ciofeeder.domain.constant;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * 年代区分
 */
@RequiredArgsConstructor
@Getter
public enum Age {

    /**
     * 65歳以上
     */
    OLD("65-", "old", 2),
    /**
     * 65歳未満
     */
    YOUNG("-64", "young", 1),
    /**
     * 不明
     */
    UNKNOWN("UNK", "unknown", 0);

    /**
     * 都道府県コード値
     */
    @NonNull
    private final String value;

    /**
     * firestoreのキー値
     */
    @NonNull
    private final String firestoreKey;

    /**
     * firestoreの値
     */
    @NonNull
    private final Integer firestoreValue;

    /**
     * 年代区分に対応するenumを取得する
     *
     * @param value 年代区分
     * @return enum
     * @throws IllegalArgumentException 年代区分が不正な値の場合の例外
     */
    @NonNull
    public static Age getAge(final String value) throws IllegalArgumentException {
        return Arrays.stream(Age.values())
                .filter(age -> age.getValue().equals(value))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}

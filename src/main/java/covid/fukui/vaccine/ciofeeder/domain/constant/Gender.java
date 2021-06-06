package covid.fukui.vaccine.ciofeeder.domain.constant;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * 性別
 */
@RequiredArgsConstructor
@Getter
public enum Gender {

    /**
     * 男性
     */
    MALE("M", "male", 1),
    /**
     * 女性
     */
    FEMALE("F", "female", 2),
    /**
     * 不明
     */
    UNKNOWN("U", "unknown", 0);

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
     * 性別に対応するenumを取得する
     *
     * @param value 性別
     * @return enum
     * @throws IllegalArgumentException 性別が不正な値の場合の例外
     */
    @NonNull
    public static Gender getAge(final String value) throws IllegalArgumentException {
        return Arrays.stream(Gender.values())
                .filter(gender -> gender.getValue().equals(value))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}

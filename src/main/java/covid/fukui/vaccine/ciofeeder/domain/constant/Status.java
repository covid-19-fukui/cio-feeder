package covid.fukui.vaccine.ciofeeder.domain.constant;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * 接種ステータス
 */
@RequiredArgsConstructor
@Getter
public enum Status {

    /**
     * 1回目接種または接種中となる接種ステータス
     */
    FIRST(1, "first"),
    /**
     * 2回目接種または接種完了となる接種ステータス
     */
    SECOND(2, "second");

    @NonNull
    private final Integer code;

    /**
     * firestoreのキー
     */
    @NonNull
    private final String key;

    /**
     * 接種ステータスに対応するenumを取得する
     *
     * @param value 接種ステータス
     * @return enum
     * @throws IllegalArgumentException 接種ステータスが不正な値の場合の例外
     */
    @NonNull
    public static Status getStatus(final Integer value) throws IllegalArgumentException {
        return Arrays.stream(Status.values())
                .filter(status -> status.getCode().equals(value))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}

package covid.fukui.vaccine.ciofeeder.domain.model;

import covid.fukui.vaccine.ciofeeder.domain.constant.Age;
import covid.fukui.vaccine.ciofeeder.domain.constant.Gender;
import covid.fukui.vaccine.ciofeeder.domain.constant.Prefecture;
import covid.fukui.vaccine.ciofeeder.domain.constant.Status;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
public class Vaccination implements Serializable {

    private static final long serialVersionUID = 5258959520904724154L;

    /**
     * 接種日
     */
    @NonNull
    private final LocalDate date;

    /**
     * 都道府県コード
     */
    @NonNull
    private final Prefecture prefecture;

    /**
     * 性別区分
     */
    @NonNull
    private final Gender gender;

    /**
     * 年代区分
     */
    @NonNull
    private final Age age;

    /**
     * 医療従事者フラグ
     */
    @NonNull
    private final boolean medicalWorker;

    /**
     * 接種ステータス区分
     */
    @NonNull
    private final Status status;

    /**
     * 接種人数
     */
    @NonNull
    private final Integer count;
}

package covid.fukui.vaccine.ciofeeder.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * cioのレスポンスに含まれる接種データ
 */
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class VaccinationResponse implements Serializable {

    private static final long serialVersionUID = -1154317251043476839L;

    /**
     * 接種日: YYYY-MM-DD 形式の接種日
     */
    private String date;

    /**
     * 都道府県コード: 	全国地方公共団体コードに基づく都道府県コードの先頭2文字
     */
    private String prefecture;

    /**
     * 性別区分: 接種者群の性別を表す区分値
     * M: 男性
     * F: 女性
     * U: 不明
     */
    private String gender;

    /**
     * 年代区分: 接種者群の年代を表す区分値
     * -64: 64歳以下
     * 65-: 65歳以上
     * UNK: 不明
     */
    private String age;

    /**
     * 医療従事者フラグ: 医療従事者の接種かどうかを表すフラグ
     * true: 医療従事者
     * false: 一般接種者
     */
    @JsonProperty("medical_worker")
    private Boolean medicalWorker;

    /**
     * 接種ステータス区分: 接種ステータスを表す区分値
     * 1: 1回目接種または接種中となる接種ステータス
     * 2: 2回目接種または接種完了となる接種ステータス
     * 2回で接種が完了するワクチンの場合、1回目の接種時のステータスは1となり、2回目の接種時のステータスは2となる
     * 1回で接種が完了するワクチンの場合、接種時のステータスは2となる
     */
    private Integer status;

    /**
     * 接種回数: (接種日, 都道府県コード, 性別区分, 年代区分, 医療従事者フラグ, 接種ステータス区分) の組に該当するワクチン接種回数
     */
    private Integer count;
}

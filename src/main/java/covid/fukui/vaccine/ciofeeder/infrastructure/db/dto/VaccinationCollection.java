package covid.fukui.vaccine.ciofeeder.infrastructure.db.dto;


import com.google.cloud.firestore.annotation.DocumentId;
import java.io.Serializable;
import java.util.Date;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.cloud.gcp.data.firestore.Document;
import org.springframework.lang.NonNull;

@Document(collectionName = "vaccination")
@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
public class VaccinationCollection implements Serializable {

    private static final long serialVersionUID = -4573437259033294516L;

    /**
     * firestoreのキー
     */
    @DocumentId
    @NonNull
    private final String vaccinationKey;

    /**
     * 接種日
     */
    @NonNull
    private final Date date;

    /**
     * 都道府県コード
     */
    @NonNull
    private final Integer prefecture;

    /**
     * 全体の接種数(一回目)
     */
    @NonNull
    private final Integer countFirst;

    /**
     * 全体の接種数(二回目)
     */
    @NonNull
    private final Integer countSecond;

    /**
     * 全体の接種率(一回目)
     */
    @NonNull
    private final Float percentageFirst;

    /**
     * 全体の接種率(二回目)
     */
    @NonNull
    private final Float percentageSecond;

    /**
     * 男性の接種数(一回目)
     */
    @NonNull
    private final Integer countMaleFirst;

    /**
     * 男性の接種数(二回目)
     */
    @NonNull
    private final Integer countMaleSecond;

    /**
     * 男性の接種率(一回目)
     */
    @NonNull
    private final Float percentageMaleFirst;

    /**
     * 男性の接種率(二回目)
     */
    @NonNull
    private final Float percentageMaleSecond;

    /**
     * 女性の接種数(一回目)
     */
    @NonNull
    private final Integer countFemaleFirst;

    /**
     * 女性の接種数(二回目)
     */
    @NonNull
    private final Integer countFemaleSecond;

    /**
     * 女性の接種率(一回目)
     */
    @NonNull
    private final Float percentageFemaleFirst;

    /**
     * 女性の接種率(二回目)
     */
    @NonNull
    private final Float percentageFemaleSecond;

    /**
     * 65歳以上の接種数(一回目)
     */
    @NonNull
    private final Integer countOldFirst;

    /**
     * 65歳以上の接種数(二回目)
     */
    @NonNull
    private final Integer countOldSecond;

    /**
     * 65歳以上の接種率(一回目)
     */
    @NonNull
    private final Float percentageOldFirst;

    /**
     * 65歳以上の接種率(二回目)
     */
    @NonNull
    private final Float percentageOldSecond;

    /**
     * 64歳以下の接種数(一回目)
     */
    @NonNull
    private final Integer countYoungFirst;

    /**
     * 64歳以下の接種数(二回目)
     */
    @NonNull
    private final Integer countYoungSecond;

    /**
     * 64歳以下の接種率(一回目)
     */
    @NonNull
    private final Float percentageYoungFirst;

    /**
     * 64歳以下の接種率(二回目)
     */
    @NonNull
    private final Float percentageYoungSecond;
}

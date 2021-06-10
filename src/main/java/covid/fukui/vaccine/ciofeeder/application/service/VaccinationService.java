package covid.fukui.vaccine.ciofeeder.application.service;

import com.google.cloud.Timestamp;
import covid.fukui.vaccine.ciofeeder.domain.constant.Age;
import covid.fukui.vaccine.ciofeeder.domain.constant.FukuiPopulation;
import covid.fukui.vaccine.ciofeeder.domain.constant.Gender;
import covid.fukui.vaccine.ciofeeder.domain.constant.Prefecture;
import covid.fukui.vaccine.ciofeeder.domain.constant.Status;
import covid.fukui.vaccine.ciofeeder.domain.model.Vaccination;
import covid.fukui.vaccine.ciofeeder.domain.repository.api.CioRepository;
import covid.fukui.vaccine.ciofeeder.domain.repository.db.FirestoreRepository;
import covid.fukui.vaccine.ciofeeder.infrastructure.api.dto.VaccinationResponse;
import covid.fukui.vaccine.ciofeeder.infrastructure.db.dto.VaccinationCollection;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * ワクチン接種状況を集計し保存するサービスクラス
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VaccinationService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final CioRepository cioRepository;

    private final FirestoreRepository firestoreRepository;

    private final Clock clock;

    /**
     * 接種データを取得する
     *
     * @return 接種データ
     */
    @NonNull
    public Mono<VaccinationCollection> saveVaccination() {

        // 現在の日付の前日分を更新
        final LocalDate now = LocalDate.now(clock).minusDays(1);

        return cioRepository.getVaccination()
                .map(this::buildVaccination)
                // 福井県に絞る
                .filter(vaccination -> vaccination.getPrefecture().isFukui())
                .collect(Collectors.toList())
                .map(vaccinations -> buildVaccinationCollection(vaccinations, now))
                .flatMap(firestoreRepository::insertVaccination);
    }

    /**
     * 接種率を計算する
     *
     * @param count      接種回数
     * @param population 人口
     * @return 接種率
     */
    @NonNull
    private Float calculatePercentage(final Integer count, final Integer population) {
        return Float.valueOf(count) * 100 / population;
    }

    /**
     * 接種ステータスごとの接種数集計
     *
     * @param vaccinationList 接種データのリスト
     * @return 接種ステータスごとに集計された接種数
     */
    @NonNull
    private Map<Status, Integer> countVaccinationEachStatus(
            final List<Vaccination> vaccinationList) {
        return vaccinationList.stream()
                .collect(Collectors.groupingBy(Vaccination::getStatus,
                        Collectors.summingInt(Vaccination::getCount)));
    }

    /**
     * 接種ステータスごとの年代別接種数集計
     *
     * @param vaccinationList 接種データのリスト
     * @return 接種ステータスごとに集計された年代別接種数
     */
    @NonNull
    private Map<Status, Map<Age, Integer>> countVaccinationEachAge(
            final List<Vaccination> vaccinationList) {
        return vaccinationList.stream().collect(Collectors.groupingBy(Vaccination::getStatus,
                Collectors.groupingBy(Vaccination::getAge,
                        Collectors.summingInt(Vaccination::getCount))));
    }

    /**
     * 接種ステータスごとの性別別接種数集計
     *
     * @param vaccinationList 接種データのリスト
     * @return 接種ステータスごとに集計された性別別接種数
     */
    @NonNull
    private Map<Status, Map<Gender, Integer>> countVaccinationEachGender(
            final List<Vaccination> vaccinationList) {
        return vaccinationList.stream().collect(Collectors.groupingBy(Vaccination::getStatus,
                Collectors.groupingBy(Vaccination::getGender,
                        Collectors.summingInt(Vaccination::getCount))));
    }

    /**
     * 接種データへの変換
     *
     * @param vaccinationResponse cioレスポンス
     * @return 接種データ
     */
    @NonNull
    private Vaccination buildVaccination(final VaccinationResponse vaccinationResponse) {
        return Vaccination.builder()
                .date(getLocalDate(vaccinationResponse.getDate()))
                .prefecture(Prefecture.getPrefecture(vaccinationResponse.getPrefecture()))
                .gender(Gender.getAge(vaccinationResponse.getGender()))
                .age(Age.getAge(vaccinationResponse.getAge()))
                .medicalWorker(vaccinationResponse.getMedicalWorker())
                .status(Status.getStatus(vaccinationResponse.getStatus()))
                .count(vaccinationResponse.getCount())
                .build();
    }

    /**
     * firestore形式の集計データへの変換
     *
     * @param vaccinations 接種データ
     * @param now          更新対象の日時
     * @return firestore形式の接種データ
     */
    @NonNull
    private VaccinationCollection buildVaccinationCollection(final List<Vaccination> vaccinations,
                                                             final LocalDate now) {

        // firestoreのキー
        final String key = String.format("%s-%s", Prefecture.FUKUI.getFirestoreKey(),
                now.format(DATE_FORMATTER));

        // 更新対象の日付
        final Timestamp date =
                Timestamp.of(Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // 当日の接種データ
        final List<Vaccination> vaccinationsToday = vaccinations.stream()
                .filter(vaccination -> now.equals(vaccination.getDate())).collect(
                        Collectors.toList());

        // 集計処理
        final Map<Status, Integer> countEachStatus =
                countVaccinationEachStatus(vaccinationsToday);

        final Map<Status, Map<Age, Integer>> countEachAgeByStatus =
                countVaccinationEachAge(vaccinationsToday);

        final Map<Status, Map<Gender, Integer>> countEachGenderByStatus =
                countVaccinationEachGender(vaccinationsToday);

        final Map<Status, Integer> totalEachStatus =
                countVaccinationEachStatus(vaccinations);

        final Map<Status, Map<Age, Integer>> totalEachAgeByStatus =
                countVaccinationEachAge(vaccinations);

        final Map<Status, Map<Gender, Integer>> totalEachGenderByStatus =
                countVaccinationEachGender(vaccinations);

        // 集計データ(全体)
        final Integer countFirst = floorCount(countEachStatus.get(Status.FIRST));
        final Integer countSecond = floorCount(countEachStatus.get(Status.SECOND));
        final Float percentageFirst =
                calculatePercentage(totalEachStatus.get(Status.FIRST),
                        FukuiPopulation.ALL.getCount());
        final Float percentageSecond =
                calculatePercentage(totalEachStatus.get(Status.SECOND),
                        FukuiPopulation.ALL.getCount());

        // 集計データ(男女別)
        final Map<Gender, Integer> countEachGenderFirst = countEachGenderByStatus.get(Status.FIRST);
        final Map<Gender, Integer> countEachGenderSecond =
                countEachGenderByStatus.get(Status.SECOND);
        // 男性
        final Integer countMaleFirst =
                floorCount(Objects.nonNull(countEachGenderFirst) ?
                        countEachGenderFirst.get(Gender.MALE) : 0);
        final Integer countMaleSecond =
                floorCount(Objects.nonNull(countEachGenderSecond) ?
                        countEachGenderSecond.get(Gender.MALE) : 0);
        final Float percentageMaleFirst =
                calculatePercentage(totalEachGenderByStatus.get(Status.FIRST).get(Gender.MALE),
                        FukuiPopulation.MALE.getCount());
        final Float percentageMaleSecond =
                calculatePercentage(totalEachGenderByStatus.get(Status.SECOND).get(Gender.MALE),
                        FukuiPopulation.MALE.getCount());

        // 女性
        final Integer countFemaleFirst =
                floorCount(Objects.nonNull(countEachGenderFirst) ?
                        countEachGenderFirst.get(Gender.FEMALE) : 0);
        final Integer countFemaleSecond =
                floorCount(Objects.nonNull(countEachGenderSecond) ?
                        countEachGenderSecond.get(Gender.FEMALE) : 0);
        final Float percentageFemaleFirst =
                calculatePercentage(totalEachGenderByStatus.get(Status.FIRST).get(Gender.FEMALE),
                        FukuiPopulation.FEMALE.getCount());
        final Float percentageFemaleSecond =
                calculatePercentage(totalEachGenderByStatus.get(Status.SECOND).get(Gender.FEMALE),
                        FukuiPopulation.FEMALE.getCount());

        // 集計データ(年代別)
        final Map<Age, Integer> countEachAgeFirst = countEachAgeByStatus.get(Status.FIRST);
        final Map<Age, Integer> countEachAgeSecond = countEachAgeByStatus.get(Status.SECOND);

        // 64歳以下
        final Integer countYoungFirst =
                floorCount(
                        Objects.nonNull(countEachAgeFirst) ? countEachAgeFirst.get(Age.YOUNG) : 0);
        final Integer countYoungSecond =
                floorCount(
                        Objects.nonNull(countEachAgeSecond) ? countEachAgeSecond.get(Age.YOUNG) : 0);
        final Float percentageYongFirst =
                calculatePercentage(totalEachAgeByStatus.get(Status.FIRST).get(Age.YOUNG),
                        FukuiPopulation.YOUNG.getCount());
        final Float percentageYongSecond =
                calculatePercentage(totalEachAgeByStatus.get(Status.SECOND).get(Age.YOUNG),
                        FukuiPopulation.YOUNG.getCount());

        // 65歳以上
        final Integer countOldFirst =
                floorCount(Objects.nonNull(countEachAgeFirst) ? countEachAgeFirst.get(Age.OLD) : 0);
        final Integer countOldSecond =
                floorCount(
                        Objects.nonNull(countEachAgeSecond) ? countEachAgeSecond.get(Age.OLD) : 0);
        final Float percentageOldFirst =
                calculatePercentage(totalEachAgeByStatus.get(Status.FIRST).get(Age.OLD),
                        FukuiPopulation.OLD.getCount());
        final Float percentageOldSecond =
                calculatePercentage(totalEachAgeByStatus.get(Status.SECOND).get(Age.OLD),
                        FukuiPopulation.OLD.getCount());

        // firestore用クラスに変換
        return VaccinationCollection.builder()
                .vaccinationKey(key)
                .prefecture(Prefecture.FUKUI.getFirestoreValue())
                .date(date)
                .countFirst(countFirst)
                .countSecond(countSecond)
                .percentageFirst(percentageFirst)
                .percentageSecond(percentageSecond)
                .countMaleFirst(countMaleFirst)
                .countMaleSecond(countMaleSecond)
                .percentageMaleFirst(percentageMaleFirst)
                .percentageMaleSecond(percentageMaleSecond)
                .countFemaleFirst(countFemaleFirst)
                .countFemaleSecond(countFemaleSecond)
                .percentageFemaleFirst(percentageFemaleFirst)
                .percentageFemaleSecond(percentageFemaleSecond)
                .countYoungFirst(countYoungFirst)
                .countYoungSecond(countYoungSecond)
                .percentageYoungFirst(percentageYongFirst)
                .percentageYoungSecond(percentageYongSecond)
                .countOldFirst(countOldFirst)
                .countOldSecond(countOldSecond)
                .percentageOldFirst(percentageOldFirst)
                .percentageOldSecond(percentageOldSecond)
                .build();
    }

    /**
     * 集計された値が0未満orNullの場合、0に補正する
     *
     * @param count 集計値
     * @return 補正された値
     */
    @NonNull
    private Integer floorCount(final Integer count) {
        return NumberUtils.max(Objects.requireNonNullElse(count, 0), 0);
    }

    /**
     * 文字列からLocalDate(yyyy-MM-dd)へ変換する
     *
     * @param date 文字列型の日付
     * @return LocalDate型の日付
     * @throws DateTimeParseException 日付変換に失敗した場合の例外
     */
    @NonNull
    private LocalDate getLocalDate(final String date) throws DateTimeParseException {
        return LocalDate.parse(date, DATE_FORMATTER);
    }
}

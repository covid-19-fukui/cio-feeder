package covid.fukui.vaccine.ciofeeder.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * 福井県の人口
 */
@Getter
@RequiredArgsConstructor
public enum FukuiPopulation {

    MALE(379141),
    FEMALE(400912),
    YOUNG(547581),
    OLD(232456),
    ALL(780053);

    @NonNull
    private final Integer count;
}

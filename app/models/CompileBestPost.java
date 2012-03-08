package models;

import com.google.common.base.Function;
import org.omg.CORBA.portable.ValueOutputStream;

import javax.annotation.Nullable;

/**
 * .
 *
 * @author fsznajderman
 *         date :  03/03/12
 */
public class CompileBestPost implements Function<ActivityWrapper, Long> {

    private static final int COEFFICIENT_PLUS_UN = 1;
    private static final int COEFFICIENT_SHARED = 3;

    private static final int COEFFICIENT_COMMENT = 2;

    public Long apply(@Nullable ActivityWrapper activityWrapper) {

        Long rank = 0l;
        Long nbPlusUn = activityWrapper.getNbPlusOners();
        Long nbShared = activityWrapper.getNbReshared();

        rank = (nbPlusUn != null ? nbPlusUn * COEFFICIENT_PLUS_UN : 0) + (nbShared != null ? nbShared * COEFFICIENT_SHARED : 0);


        return rank;
    }
}

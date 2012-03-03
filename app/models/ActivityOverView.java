package models;


import com.google.common.base.Function;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

/**
 * Gives an overview of all informations which are required in GUI .
 *
 * @author fsznajderman
 *         date :  03/03/12
 */
public class ActivityOverView {

    private List<ActivityWrapper> activityWrappers;


    public Long getCompileAveragePlusOnePage() {


        Function<Iterator<ActivityWrapper>, Long> f = new Function<Iterator<ActivityWrapper>, Long>() {

            public Long apply(@Nullable Iterator<ActivityWrapper> activityWrappers) {

                if (!activityWrappers.hasNext()) {
                    return Long.getLong("0");
                } else {

                    Long nbPlusOne = activityWrappers.next().getNbPlusOners();
                    System.out.println(nbPlusOne);
                    Long nb = (nbPlusOne != null ? nbPlusOne : 0);
                    System.out.println(activityWrappers);

                    Long r = apply(activityWrappers);

                    return (r!= null ? r : 0) + nb;
                }

            }
        };
        return f.apply(activityWrappers.iterator());
    }


    public List<ActivityWrapper> getActivityWrappers() {
        return activityWrappers;
    }

    public void setActivityWrappers(List<ActivityWrapper> activityWrappers) {
        this.activityWrappers = activityWrappers;
    }
}

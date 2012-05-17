package models;


import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import models.domain.Article;
import models.utils.HtmlUtils;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Gives an overview of all informations which are required in GUI .
 *
 * @author fsznajderman
 *         date :  03/03/12
 */
public class ActivitiesHelper {

    private List<ActivityWrapper> activityWrappers;
    private static final Long ZERO = 0l;


    public String getBestNamePost() {
        ActivityWrapper aw = Ordering.natural().onResultOf(new CompileBestPost()).greatestOf(activityWrappers, 1).get(0);
        return HtmlUtils.getActivityAuthor(aw);
    }

    public String getBestTitrePost() {
        ActivityWrapper aw = Ordering.natural().onResultOf(new CompileBestPost()).greatestOf(activityWrappers, 1).get(0);

        return HtmlUtils.getActivityTitle(aw);
    }

    public String getPlusOneMatrix() {
        Map<Date, Long> matrix = Maps.newTreeMap();
        for (ActivityWrapper aw : activityWrappers) {
            matrix.put(new Date(aw.getPublicationDate().getValue()), aw.getNbPlusOners());
        }

        return Joiner.on(",").join(matrix.values());
    }

    public String getSharedMatrix() {
        Map<Date, Long> matrix = Maps.newTreeMap();

        for (ActivityWrapper aw : activityWrappers) {
            matrix.put(new Date(aw.getPublicationDate().getValue()), aw.getNbReshared());
        }

        return Joiner.on(",").join(matrix.values());
    }


    public Collection<String> getTitleMatrix() {
        Map<Date, String> matrix = Maps.newTreeMap();
        for (ActivityWrapper aw : activityWrappers) {
            matrix.put(new Date(aw.getPublicationDate().getValue()), aw.getTitle());
        }


        return matrix.values();
    }

    /**
     * compile each plus one
     *
     * @return
     */
    public Long getCompileSumPlusOnePage() {
        Function<Iterator<ActivityWrapper>, Long> f = recurssiveFunctionOnePlus();
        return f.apply(activityWrappers.iterator());
    }

    /**
     * Compile all time that one post has shared
     *
     * @return
     */
    public Long getCompileSumShared() {
        Function<Iterator<ActivityWrapper>, Long> f = recurssiveFunctionShared();
        return f.apply(activityWrappers.iterator());
    }

    private Function<Iterator<ActivityWrapper>, Long> recurssiveFunctionShared() {
        return new Function<Iterator<ActivityWrapper>, Long>() {

            public Long apply(@Nullable Iterator<ActivityWrapper> activityWrappers) {

                if (!activityWrappers.hasNext()) {
                    return ZERO;
                } else {

                    Long nbPlusOne = activityWrappers.next().getNbReshared();

                    final Long nb = (nbPlusOne != null ? nbPlusOne : ZERO);
                    final Long r = apply(activityWrappers);

                    return (r != null ? r : ZERO) + nb;
                }

            }
        };
    }


    private Function<Iterator<ActivityWrapper>, Long> recurssiveFunctionOnePlus() {
        return new Function<Iterator<ActivityWrapper>, Long>() {

            public Long apply(@Nullable Iterator<ActivityWrapper> activityWrappers) {

                if (!activityWrappers.hasNext()) {
                    return ZERO;
                } else {

                    Long nbPlusOne = activityWrappers.next().getNbPlusOners();
                    final Long nb = (nbPlusOne != null ? nbPlusOne : ZERO);
                    final Long r = apply(activityWrappers);

                    return (r != null ? r : ZERO) + nb;
                }

            }
        };
    }


    public void setActivityWrappers(List<ActivityWrapper> activityWrappers) {
        this.activityWrappers = activityWrappers;
    }

    public List<Article> articlesFilter(List<Article> articles) {

        final Set<String> googleIds = Sets.newHashSet();
        final List<Article> finalArticles = Lists.newArrayList();

        for (Article a : articles) {
            googleIds.add(a.getGoogleId());

            Collection<Article> temp = Collections2.filter(articles, new GoogleIdPredicate(a.getGoogleId()));



        }

        return null;


    }
}

class GoogleIdPredicate implements Predicate<Article> {

    private String googleIdRef = null;

    public GoogleIdPredicate(String googleIdRef) {
        Preconditions.checkNotNull(googleIdRef, "Ref can not be null!!");
        this.googleIdRef = googleIdRef;
    }

    public boolean apply(@Nullable Article article) {

        Preconditions.checkNotNull(article, "article can not be null here!!");

        return article.getGoogleId().equalsIgnoreCase(googleIdRef);
    }

}
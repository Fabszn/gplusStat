package jobs;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import models.ActivitiesHelper;
import models.ActivityWrapper;
import models.domain.Article;
import models.domain.Statistiques;
import models.domain.Tag;
import models.utils.ConfUtil;
import models.utils.HtmlUtils;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.On;

import java.io.IOException;
import java.util.*;

/**
 * .
 *
 * @author fsznajderman
 *         date :  01/05/12
 */
@On(" * 0/30 * * * ?")
public class RefreshDatasFromGPlusJobs extends Job {

    final static JsonFactory jsonFactory = new JacksonFactory();
    final static HttpTransport httpTransport = new NetHttpTransport();

    @Override
    public void doJob() throws Exception {
        Logger.info("start shedule job");

        final Plus plus = new Plus(httpTransport, jsonFactory);
        // When we do not specify access tokens, we must specify our API key
        // instead
        String key = ConfUtil.getValueFromGplusConf("google.key");

        plus.setKey(key);
        final Plus.Activities.List activitiesFromGplus = plus.activities.list(ConfUtil.getValueFromGplusConf("google.id.page"), "public");

        // Fetch the first page of activities

        activitiesFromGplus.setMaxResults(100L);
        // Pro tip: Use partial responses to improve response time considerably
        activitiesFromGplus.setFields("nextPageToken,items(id,url,published,object(content,plusoners,resharers))");

        final Set<Article> fromGPlus = Sets.newHashSet();
        final Set<Tag> tagFromGplus = Sets.newHashSet();
        final List<ActivityWrapper> localActivityWrappers = Lists.newArrayList();
        try {

            ActivityFeed feed = activitiesFromGplus.execute();
            List<Activity> activities = feed.getItems();
            while (activities != null) {
                Logger.debug("turn " + activities.size());
                // Execute and process the next page request
                for (final Activity activity : activities) {

                    final ActivityWrapper wrapper = new ActivityWrapper(activity);
                    localActivityWrappers.add(wrapper);

                    fromGPlus.add(wrapper.getArticle());
                    tagFromGplus.addAll(wrapper.getTags());

                }
                if (feed.getNextPageToken() == null) {
                    break;
                }
                activitiesFromGplus.setPageToken(feed.getNextPageToken());

                // Execute and process the next page request
                feed = activitiesFromGplus.execute();
                activities = feed.getItems();
            }
        } catch (final IOException e) {
            Logger.error(e.getMessage(),e);
        }
        saveTags(tagFromGplus, localActivityWrappers);

        List<Article> fromDBList = Article.findAll();
        //reset all articles by setting their current field to Boolean.FALSE.
        CollectionUtils.forAllDo(fromDBList, new Closure() {
            public void execute(Object o) {
                ((Article) o).setCurrent(Boolean.FALSE);
                ((Article) o).save();
            }
        });

        for (final Article a : fromGPlus) {

            if (!fromDBList.contains(a)) {
                a.setCurrent(Boolean.TRUE);
                a.save();

            } else {

                Article c = (Article) CollectionUtils.find(fromDBList, new Predicate() {
                    public boolean evaluate(Object o) {
                        Article article = (Article) o;
                        return a.equals(article);
                    }
                });
                c.setCurrent(Boolean.TRUE);
                c.save();
            }
        }
        createStatistique(localActivityWrappers);

        Logger.info("End Job");
    }

    private void saveTags(Set<Tag> tagFromGplus, final List<ActivityWrapper> localActivityWrappers) {
        final List<Tag> tags = Tag.findAll();
        //manage link between tag and article
        CollectionUtils.forAllDo(tagFromGplus, new Closure() {
            public void execute(Object o) {
                final Tag currentTag = (Tag) o;
                CollectionUtils.forAllDo(localActivityWrappers, new Closure() {
                    public void execute(Object o) {
                        ActivityWrapper activityWrapper = (ActivityWrapper) o;
                        if (activityWrapper.getTags().contains(currentTag)) {
                            currentTag.getArticleIds().add(activityWrapper.getGoogleId());
                        }
                    }
                });
                if (!tags.contains(currentTag)) {
                    currentTag.save();
                }
            }
        });
    }

    private void createStatistique(final List<ActivityWrapper> activityWrappers) {
        final Statistiques s = new Statistiques();
        final ActivitiesHelper ah = new ActivitiesHelper();
        ah.setActivityWrappers(activityWrappers);

        s.setBestNamePost(ah.getBestNamePost());
        s.setBestTitlePost(ah.getBestTitrePost());
        s.setPlusOneMatrix(ah.getPlusOneMatrix());
        s.setSharedMatrix(ah.getSharedMatrix());
        s.setSumPlusOne(ah.getCompileSumPlusOnePage());
        s.setSumShared(ah.getCompileSumShared());

        final List<Statistiques> stats = Statistiques.findAll();
        CollectionUtils.forAllDo(stats, new Closure() {
            public void execute(Object o) {
                Statistiques currentStat = (Statistiques) o;
                currentStat.setCurrent(Boolean.FALSE);
                currentStat.save();
            }
        });

        if (!stats.contains(s)) {

            s.setCurrent(Boolean.TRUE);
            s.save();
        } else {

            final Statistiques c = (Statistiques) CollectionUtils.find(stats, new Predicate() {
                public boolean evaluate(Object o) {
                    Statistiques currentStat = (Statistiques) o;
                    return s.equals(currentStat);
                }
            });
            c.setCurrent(Boolean.TRUE);
            c.save();
        }


    }
}

package jobs;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import models.ActivitiesHelper;
import models.ActivityWrapper;
import models.domain.Article;
import models.domain.Statistiques;
import models.utils.HtmlUtils;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.On;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * .
 *
 * @author fsznajderman
 *         date :  01/05/12
 */
@On("0/30 * * * * ?")
public class RefreshDatasFromGPlusJobs extends Job {

    final static JsonFactory jsonFactory = new JacksonFactory();
    final static HttpTransport httpTransport = new NetHttpTransport();

    @Override
    public void doJob() throws Exception {
        System.out.println("start shedule job");

        final Plus plus = new Plus(httpTransport, jsonFactory);
        // When we do not specify access tokens, we must specify our API key
        // instead
        String key = HtmlUtils.getValueFromGplusConf("google.key");

        plus.setKey(key);
        final Plus.Activities.List listActivities = plus.activities.list(HtmlUtils.getValueFromGplusConf("google.id.page"), "public");

        // Fetch the first page of activities

        listActivities.setMaxResults(100L);
        // Pro tip: Use partial responses to improve response time considerably
        listActivities.setFields("items(id,url,published,object(content,plusoners,resharers))");

        Set<Article> fromGPlus = new HashSet<Article>();

        final List<ActivityWrapper> activityWrappers = Lists.newArrayList();
        try {
            // Execute and process the next page request
            final ActivityFeed feed = listActivities.execute();

            for (final Activity activity : feed.getItems()) {

                final ActivityWrapper wrapper = new ActivityWrapper(activity);

                activityWrappers.add(wrapper);
                Article a = new Article(wrapper.getAuthor(), wrapper.getContent(), wrapper.getGoogleId(), wrapper.getNbPlusOners(), wrapper.getPublicationDate().getValue(), wrapper.getNbReshared(), wrapper.getTitle());

                fromGPlus.add(a);


            }
        } catch (final IOException e) {
            e.printStackTrace();
        }

        List<Article> fromDBList = Article.findAll();


        for (Article a : fromGPlus) {

            if (!fromDBList.contains(a)) {
                System.out.println("save " + a.getTitle());
                a.save();

            }
        }
        createStatistique(activityWrappers);

        System.out.println("End Job");
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
        List<Statistiques> stats = Statistiques.findAll();

        if (!stats.contains(s)) {

            System.out.println(s.toString());
            s.save();
        }


    }
}

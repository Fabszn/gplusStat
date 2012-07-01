package controllers;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import models.ActivityWrapper;
import models.domain.Article;
import models.domain.Tag;
import models.utils.ConfUtil;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import play.mvc.Controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * .
 *
 * @author fsznajderman
 *         date :  01/07/12
 */
public class MajDB extends Controller{

    final static JsonFactory jsonFactory = new JacksonFactory();
    final static HttpTransport httpTransport = new NetHttpTransport();

    public static void update(){

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
                System.out.println("turn " + activities.size());
                // Execute and process the next page request
                for (final Activity activity : activities) {

                    final ActivityWrapper wrapper = new ActivityWrapper(activity);
                    localActivityWrappers.add(wrapper);

                    fromGPlus.add(wrapper.getArticle());


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
            e.printStackTrace();
        }

        CollectionUtils.forAllDo(localActivityWrappers,new Closure() {
            public void execute(Object o) {
                ActivityWrapper w = (ActivityWrapper)o;
                final String googleId = w.getGoogleId();

                final Article a = (Article)Article.q().filter("googleId",googleId)._get();

                //a.setContent(w.getContent());
                a.setUrl(w.getUrl());
                a.setTitle(w.getTitle());
                System.out.println(a + " " + a.getUrl() );

                a.o().set("title",w.getTitle()).update("googleId",googleId);
                a.o().set("url",w.getUrl()).update("googleId",googleId);

            }
        });


    }
}

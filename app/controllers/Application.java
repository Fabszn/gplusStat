package controllers;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import models.ActivitiesHelper;
import models.ActivityWrapper;
import models.ViewInformations;
import models.domain.Article;
import models.domain.Statistiques;
import models.domain.Tag;
import models.utils.ConfUtil;
import play.Logger;
import play.Play;
import play.mvc.Controller;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Application extends Controller {
    final static JsonFactory jsonFactory = new JacksonFactory();
    final static HttpTransport httpTransport = new NetHttpTransport();


    @Deprecated
    public static void index() {


        final Plus plus = new Plus(httpTransport, jsonFactory);
        // When we do not specify access tokens, we must specify our API key
        // instead
        String key = ConfUtil.getValueFromGplusConf("google.key");

        plus.setKey(key);
        final Plus.Activities.List listActivities = plus.activities.list(ConfUtil.getValueFromGplusConf("google.id.page"), "public");

        // Fetch the first page of activities

        listActivities.setMaxResults(100L);
        // Pro tip: Use partial responses to improve response time considerably
        listActivities.setFields("items(id,url,published,object(content,plusoners,resharers))");

        ActivityFeed feed = null;
        final List<ActivityWrapper> activityWrappers = Lists.newArrayList();

        try {
            // Execute and process the next page request
            feed = listActivities.execute();

            for (final Activity activity : feed.getItems()) {

                final ActivityWrapper activityWrapper = new ActivityWrapper(activity);

                activityWrappers.add(activityWrapper);
            }
        } catch (final IOException e) {
            Logger.debug(e.getMessage(), e);
        }
        ActivitiesHelper aov = new ActivitiesHelper();
        aov.setActivityWrappers(activityWrappers);

        render(aov);
    }

    public static void index2() {


        final List<Article> articles = Article.q().filter("current", true).asList();

        final List<Statistiques> statistiques = Statistiques.q().filter("current", true).asList();

        final List<Tag> tags = Tag.findAll();
        Collection<Tag> tagsFiltered = Collections2.filter(tags, new Predicate<Tag>() {
            public boolean apply(@Nullable Tag tag) {
                return tag.getArticleIds().size() > 1;
            }
        });

        final ViewInformations viewInformations = new ViewInformations(articles, statistiques.get(statistiques.size() - 1), tagsFiltered);


        render(viewInformations);
    }

    public static void listArticlesFromLblTag(final String lblTag) {


        Tag tags = (Tag) Tag.q().filter("lblTag", lblTag)._get();


        Set<String> maps = tags.getArticleIds();
        Collection<Article> l = Collections2.transform(maps, new Function<String, Article>() {
            public Article apply(@Nullable String s) {

                return (Article) Article.q().filter("googleId", s)._get();
            }
        });
        render(l, lblTag);
    }

    public static void test() {
        final Plus plus = new Plus(httpTransport, jsonFactory);
        // When we do not specify access tokens, we must specify our API key
        // instead
        String key = ConfUtil.getValueFromGplusConf("google.key");

        plus.setKey(key);
        final Plus.Activities.List listActivities = plus.activities.list(ConfUtil.getValueFromGplusConf("google.id.page"), "public");

        // Fetch the first page of activities

        listActivities.setMaxResults(100L);
        // Pro tip: Use partial responses to improve response time considerably
        listActivities.setFields("items(id,url,published,object(content,plusoners,resharers))");

        ActivityFeed feed = null;
        final List<ActivityWrapper> activityWrappers = Lists.newArrayList();

        try {
            // Execute and process the next page request
            feed = listActivities.execute();

            for (final Activity activity : feed.getItems()) {

                final ActivityWrapper activityWrapper = new ActivityWrapper(activity);

                activityWrappers.add(activityWrapper);
            }
        } catch (final IOException e) {
            Logger.error(e.getMessage(), e);
        }
        ActivitiesHelper aov = new ActivitiesHelper();
        aov.setActivityWrappers(activityWrappers);

        ActivityWrapper w = activityWrappers.get(2);
        Article b = new Article("boboqs1", w.getContent(), "sdfdsfds1", 1, 2, 654, w.getTitle(), 321, "");
        b.save();

        Logger.debug(w.getTitle());
        render(w);
    }


}
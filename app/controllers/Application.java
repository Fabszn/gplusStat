package controllers;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Strings;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.common.collect.Lists;
import play.*;
import play.mvc.*;

import java.io.IOException;
import java.util.*;

import models.*;

public class Application extends Controller {
    final static JsonFactory jsonFactory = new JacksonFactory();
    final static HttpTransport httpTransport = new NetHttpTransport();

    public static void index() {


        final Plus plus = new Plus(httpTransport, jsonFactory);
        // When we do not specify access tokens, we must specify our API key
        // instead
        String key = Play.configuration.getProperty("google.key");

        plus.setKey(key);
        final Plus.Activities.List listActivities = plus.activities.list(Play.configuration.getProperty("google.id.page"), "public");

        // Fetch the first page of activities

        listActivities.setMaxResults(100L);
        // Pro tip: Use partial responses to improve response time considerably
        listActivities.setFields("nextPageToken,items(id,url,published,object(content,plusoners,resharers))");

        ActivityFeed feed = null;
        List<ActivityWrapper> activityWrappers = Lists.newArrayList();

        try {
            feed = listActivities.execute();

            // Keep track of the page number in case we're listing activities
            // for a user with thousands of activities. We'll limit ourselves
            // to 5 pages
            System.out.println("Start");


            // Execute and process the next page request
            feed = listActivities.execute();


            for (final Activity activity : feed.getItems()) {

                final ActivityWrapper activityWrapper = new ActivityWrapper(activity);
                activityWrappers.add(activityWrapper);
            }


        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ActivityOverView aov = new ActivityOverView();
        aov.setActivityWrappers(activityWrappers);
        ;
        render(aov);
    }


}
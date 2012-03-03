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
        plus.setKey("AIzaSyALxct37VoDkvswBYOysbMiRpO5hjVFukg");
        final Plus.Activities.List listActivities = plus.activities.list("112440333946538821016", "public");

        // Fetch the first page of activities

        listActivities.setMaxResults(5L);
        // Pro tip: Use partial responses to improve response time considerably
        listActivities.setFields("nextPageToken,items(id,url,object(content,plusoners,resharers))");

        ActivityFeed feed = null;
        List<ActivityWrapper>  activityWrappers = Lists.newArrayList();

        try {
            feed = listActivities.execute();

            // Keep track of the page number in case we're listing activities
            // for a user with thousands of activities. We'll limit ourselves
            // to 5 pages
            System.out.println("Start");
            int currentPageNumber = 0;
            while ((feed != null) && (feed.getItems() != null) && (currentPageNumber < 5)) {
                currentPageNumber++;
                listActivities.setPageToken(feed.getNextPageToken());

                // Execute and process the next page request
                feed = listActivities.execute();



                for (final Activity activity : feed.getItems()) {

                    final ActivityWrapper activityWrapper = new ActivityWrapper(activity);

                   /* System.out.println("content : " + activityWrapper.getContent());
                    System.out.println("Plus one : " + activityWrapper.getNbPlusOners());
                    System.out.println("reshared : " + activityWrapper.getNbReshared());
                    System.out.println("------------------------------------------------------");
                    System.out.println();                      */
                    activityWrappers.add(activityWrapper);
                }


            }
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("end");
        ActivityOverView aov = new ActivityOverView();
        aov.setActivityWrappers(activityWrappers);;
        render(aov);
    }

    /*public static String extractError(final HttpResponseException e) throws IOException {
        if (!Json.CONTENT_TYPE.equals(e.getResponse().getContentType())) {
            return e.getResponse().parseAsString();
        }

        final GoogleJsonError errorResponse = GoogleJsonError.parse(jsonFactory, e.getResponse());
        final StringBuilder errorReportBuilder = new StringBuilder();

        errorReportBuilder.append(errorResponse.code);
        errorReportBuilder.append(" Error: ");
        errorReportBuilder.append(errorResponse.message);

        for (final GoogleJsonError.ErrorInfo error : errorResponse.errors) {
            errorReportBuilder.append(jsonFactory.toString(error));
            errorReportBuilder.append(Strings.LINE_SEPARATOR);
        }
        return errorReportBuilder.toString();
    }*/


}
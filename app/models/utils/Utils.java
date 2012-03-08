package models.utils;

import models.ActivityWrapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * .
 *
 * @author fsznajderman
 *         date :  07/03/12
 */
public class Utils {

    public static String getActivityTitle(ActivityWrapper activityWrapper) {
        return extractContent("b", activityWrapper);

    }

    public static String getActivityAuthor(ActivityWrapper activityWrapper) {
        return extractContent("a", activityWrapper);
    }

    private static String extractContent(String baliseName, ActivityWrapper activityWrapper) {
        Document doc = Jsoup.parse(activityWrapper.getContent());

        Elements elem = doc.getElementsByTag(baliseName);
        return elem.get(0).html();

    }
}

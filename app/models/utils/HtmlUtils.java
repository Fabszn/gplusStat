package models.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import models.ActivityWrapper;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * .
 *
 * @author fsznajderman
 *         date :  07/03/12
 */
public class HtmlUtils {


    public static String getActivityTitle(ActivityWrapper activityWrapper) {
        return extractHtmlContent("b", activityWrapper);

    }

    public static String getActivityAuthor(ActivityWrapper activityWrapper) {
        return extractHtmlContent("a", activityWrapper);
    }

    private static String extractHtmlContent(String baliseName, ActivityWrapper activityWrapper) {
        String content = "Unkown";

        Document doc = Jsoup.parse(activityWrapper.getContent());

        Elements elem = doc.getElementsByTag(baliseName);

        if (elem != null && elem.size() != 0) {
            content = elem.get(0).text();
        }

        return content;

    }

    public static List<String> getTagsFromContent(String content) {

        final List<String> tags = Lists.newArrayList();
        final String regex = "#[A-Za-z]+( |)";
        final Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(content);

        while (m.find()) {

            tags.add(StringUtils.removeStart(m.group(),"#"));

        }

        return tags;


    }


}



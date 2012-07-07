import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import models.domain.Article;
import models.utils.HtmlUtils;
import org.junit.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import play.Logger;
import play.test.*;
import models.*;

public class HtmlUtilsTest {

    @Test
    public void aVeryImportantThingToTest() {
         String content = "La documentation est assez complète.                  #java #android #mobile #spring ";

        HtmlUtils.getTagsFromContent(content);

    }

    @Test
    public void regexTest(){

        String content = "#java #android #mobile #spring ";
        final String regex = "#[A-Za-z]+( |)";
        final Pattern p = Pattern.compile(regex);

       Matcher m = p.matcher(content);

        while (m.find()) {
            Logger.debug(m.group());
        }
    }

}

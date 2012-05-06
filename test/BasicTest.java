import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import models.domain.Article;
import org.junit.*;

import java.util.*;

import play.test.*;
import models.*;

public class BasicTest {

    @Test
    public void aVeryImportantThingToTest() {
        final Article a1 = new Article("a1", "", "1", 1, 1, 1, "");
        final Article a2 = new Article("a2", "", "1", 1, 1, 1, "");

        Set<Article> l1 = Sets.newHashSet(a1);
        Set<Article> l2 = Sets.newHashSet(a2,a1);

        Set<Article> r = Sets.difference(l1, l2);

        System.out.println(r.size());

        for (Article a : r) {
            System.out.println(a.getAuthor());
        }

    }

}

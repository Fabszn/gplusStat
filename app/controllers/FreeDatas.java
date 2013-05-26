package controllers;


import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import models.ViewInformations;
import models.domain.Article;
import models.domain.Statistiques;
import models.domain.Tag;
import play.mvc.Controller;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;


/**
 * .
 *
 * @author fsznajderman
 *         date :  09/06/12
 */

public class FreeDatas extends Controller {


    public static void freeStatGplus() {

        final List<Article> articles = Article.q().filter("current", true).asList();

        final List<Statistiques> statistiques = Statistiques.q().filter("current", true).asList();

        final ViewInformations viewInformations = new ViewInformations(articles, statistiques.get(statistiques.size() - 1), Lists.<Tag>newArrayList());


        Statistiques stats = statistiques.get(statistiques.size() - 1);
        stats.setTitleMatrix(viewInformations.getTitleMatrix());

        renderJSON(stats);
    }

    public static void titles() {

        final List<Article> articles = Article.q().filter("current", true).asList();


        Collection<String> titles = Collections2.transform(articles, new Function<Article, String>() {
            public String apply(@Nullable Article article) {
                return article.getTitle();
            }
        });



        renderJSON(titles);
    }

}

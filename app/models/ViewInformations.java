package models;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import models.domain.Article;
import models.domain.Statistiques;
import models.domain.Tag;
import models.utils.TagCss;

import javax.annotation.Nullable;
import java.util.*;

/**
 * .
 *
 * @author fsznajderman
 *         date :  06/05/12
 */
public class ViewInformations {


    private List<Article> articles;
    private Statistiques statistiques;
    private Collection<Tag> tags;

    public ViewInformations(final List<Article> articles,final  Statistiques statistiques,final Collection<Tag> tags) {
        Collections.sort(articles, new Comparator<Article>() {
            public int compare(Article o1, Article o2) {
                final Date d1 = new Date(o1.getPublicationDate());
                final Date d2 = new Date(o2.getPublicationDate());

                return d1.after(d2) ? -1 : 1;
            }
        });

        this.articles = articles;
        this.statistiques = statistiques;
        this.tags=tags;
    }

    public String getBestNamePost() {
        return statistiques.getBestNamePost();
    }

    public String getBestTitrePost() {
        return statistiques.getBestTitlePost();
    }

    public String getPlusOneMatrix() {

        Collection<Long> plusOnesMatrix = Collections2.transform(articles, new Function<Article, Long>() {
            public Long apply(@Nullable Article article) {
                return article.getPlusOne();
            }
        });
        return Joiner.on(",").join(plusOnesMatrix);
    }

    public String getSharedMatrix() {
        Collection<Long> sharedMatrix = Collections2.transform(articles, new Function<Article, Long>() {
            public Long apply(@Nullable Article article) {
                return article.getShared();
            }
        });
        return Joiner.on(",").join(sharedMatrix);
    }

    public String getTitleMatrix() {
        Collection<String> titleMatrix = Collections2.transform(articles, new Function<Article, String>() {
            public String apply(@Nullable Article article) {
                return article.getTitle();
            }
        });

        return Joiner.on(",").join(titleMatrix);
    }

    public String getTagCss(final int nbArticle){

        return TagCss.getClass(nbArticle);
    }

    public Collection<Tag> getTags(){
        return tags;
    }

    public Long getCompileSumPlusOnePage() {
        return statistiques.getSumPlusOne();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long getCompileSumShared() {
        return statistiques.getSumShared();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection<Article> getArticles() {
        return articles;
    }


}

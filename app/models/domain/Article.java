package models.domain;

import com.google.code.morphia.annotations.Entity;

import play.modules.morphia.Model;


/**
 * .
 *
 * @author fsznajderman
 *         date :  23/04/12
 */
@Entity
public class Article extends Model {

    private String title;
    private String googleId;
    private String content;
    private String author;
    private long publicationDate;
    private long plusOne;
    private long shared;


    public Article(String author, String content, String googleId, long plusone, long publicationDate, long shared, String title) {
        this.author = author;
        this.content = content;
        this.googleId = googleId;
        this.plusOne = plusone;
        this.publicationDate = publicationDate;
        this.shared = shared;
        this.title = title;
    }


    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getGoogleId() {
        return googleId;
    }

    public long getPlusOne() {
        return plusOne;
    }

    public long getPublicationDate() {
        return publicationDate;
    }

    public long getShared() {
        return shared;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {


        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        //if (!super.equals(o)) return false;

        Article article = (Article) o;

        if (googleId != null ? !googleId.equals(article.googleId) : article.googleId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return googleId.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Article");
        sb.append("{googleId='").append(googleId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

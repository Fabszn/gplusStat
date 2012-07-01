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
    private long insertionDate;
    private long plusOne;
    private long shared;
    private boolean current = false;
    private String url;





    public Article(String author, String content, String googleId, long plusone, long publicationDate, long shared, String title, final long insertionDate, final String url) {
        this.author = author;
        this.content = content;
        this.googleId = googleId;
        this.plusOne = plusone;
        this.publicationDate = publicationDate;
        this.shared = shared;
        this.title = title;
        this.insertionDate = insertionDate;
        this.url = url;
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

    public long getInsertionDate() {
        return insertionDate;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {

        this.current = current;
    }

    public String getUrl() {
        return url;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        Article article = (Article) o;

        if (plusOne != article.plusOne) return false;
        if (shared != article.shared) return false;
        if (googleId != null ? !googleId.equals(article.googleId) : article.googleId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (googleId != null ? googleId.hashCode() : 0);
        result = 31 * result + (int) (plusOne ^ (plusOne >>> 32));
        result = 31 * result + (int) (shared ^ (shared >>> 32));
        return result;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Article");
        sb.append("{shared=").append(shared);
        sb.append(", plusOne=").append(plusOne);
        sb.append(", googleId='").append(googleId).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

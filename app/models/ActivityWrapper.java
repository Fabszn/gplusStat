package models;

import com.google.api.client.util.DateTime;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityObject;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import models.domain.Article;
import models.domain.Tag;
import models.utils.HtmlUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Provides a simplify view of Activity
 * 
 * @author fsznajderman
 * 
 */
public class ActivityWrapper {


	private ActivityObject plusObject = null;
    private Activity activity = null;

	public ActivityWrapper(final Activity activity) {
		plusObject = activity.getPlusObject();
        this.activity = activity;
	}

	public String getContent() {
		return plusObject.getContent();

	}

	public Long getNbPlusOners() {
		if (plusObject.getPlusoners() == null) {
			System.out.println("none item");
            return 0L;
		}

		return plusObject.getPlusoners().getTotalItems();
	}

	public Long getNbReshared() {
		if (plusObject.getResharers() == null) {
			return 0L;
		}

		return plusObject.getResharers().getTotalItems();
	}

    public String getAuthor(){
        return HtmlUtils.getActivityAuthor(this);
    }


    public String getTitle(){
        return HtmlUtils.getActivityTitle(this);
    }

    public DateTime getPublicationDate() {
        return activity.getPublished();

    }

    public String getType() {
        return activity.getKind();

    }

    public String getGoogleId(){
        return activity.getId();
    }

    public Collection<Tag> getTags(){
        return Collections2.transform(HtmlUtils.getTagsFromContent(plusObject.getContent()), new Function<String,Tag>(){
            public Tag apply(@Nullable String s) {
                return new Tag(s.trim());
            }
        });
    }

    public String getUrl(){
        return activity.getUrl();
    }

    public Article getArticle(){
        return new Article(this.getAuthor(), this.getContent(), this.getGoogleId(), this.getNbPlusOners(), this.getPublicationDate().getValue(), this.getNbReshared(), this.getTitle(), new Date().getTime(),this.getUrl());
    }

}

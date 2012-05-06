package models;

import com.google.api.client.util.DateTime;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityObject;
import models.utils.HtmlUtils;

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

}

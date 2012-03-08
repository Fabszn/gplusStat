package models;

import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityObject;
import models.utils.Utils;

/**
 * Provides a simplify view of Activity
 * 
 * @author fsznajderman
 * 
 */
public class ActivityWrapper {

	private Activity activity = null;
	private ActivityObject plusObject = null;

	public ActivityWrapper(final Activity activity) {
		this.activity = activity;
		plusObject = activity.getPlusObject();
	}

	public String getContent() {
		return plusObject.getContent();

	}

	public Long getNbPlusOners() {
		if (plusObject.getPlusoners() == null) {
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
        return Utils.getActivityAuthor(this);
    }


    public String getTitle(){
        return Utils.getActivityTitle(this);
    }

}

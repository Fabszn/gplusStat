package models.domain;

import com.google.code.morphia.annotations.Entity;
import com.google.common.collect.Sets;
import models.utils.TagCss;
import play.modules.morphia.Model;

import java.util.Set;

/**
 * .Entity that describe one tag with his associated articleIds
 *
 * @author fsznajderman
 *         date :  03/06/12
 */
@Entity
public class Tag extends Model{



    private String lblTag;

    private Set<String> articleIds = Sets.newHashSet();


    public Tag(String lblTag) {
        this.lblTag = lblTag;
    }

    public Set getArticleIds() {
        return articleIds;
    }

    public String getCssClass(){
        return TagCss.getClass(articleIds.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false; }


        Tag tag = (Tag) o;

        if (lblTag != null ? !lblTag.equals(tag.lblTag) : tag.lblTag != null){ return false;}

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (lblTag != null ? lblTag.hashCode() : 0);
        return result;
    }
}

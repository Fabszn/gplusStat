package models.domain;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;

/**
 * .
 *
 * @author fsznajderman
 *         date :  17/06/12
 */
@Entity
public class Test extends Model {

    public String getNameTest() {
        return nameTest;
    }

    public void setNameTest(String nameTest) {
        this.nameTest = nameTest;
    }

    private String nameTest;


}

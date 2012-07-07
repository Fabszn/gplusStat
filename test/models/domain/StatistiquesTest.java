package models.domain;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.ehcache.Statistics;

/**
 * .
 *
 * @author fsznajderman
 *         date :  07/07/12
 */
public class StatistiquesTest extends TestCase {


    public static final String VALUE = "1234";

    public void testEqualsOk() throws Exception {
        final Statistiques s1 = new Statistiques();
        final Statistiques s2 = new Statistiques();

        s1.setBestGoogleID(VALUE);
        s1.setBestNamePost(VALUE);
        s1.setBestTitlePost(VALUE);
        s1.setPlusOneMatrix(VALUE);
        s1.setSharedMatrix(VALUE);
        s1.setSumPlusOne(1l);
        s1.setSumShared(1l);
        s1.setTitleMatrix(VALUE);


        s2.setBestGoogleID(VALUE);
        s2.setBestNamePost(VALUE);
        s2.setBestTitlePost(VALUE);
        s2.setPlusOneMatrix(VALUE);
        s2.setSharedMatrix(VALUE);
        s2.setSumPlusOne(1l);
        s2.setSumShared(1l);
        s2.setTitleMatrix(VALUE);

        Assert.assertTrue(s1.equals(s2));
    }

    public void testEqualsFail() throws Exception {
        final Statistiques s1 = new Statistiques();
        final Statistiques s2 = new Statistiques();

        s1.setBestGoogleID("Fabrice");
        s1.setBestNamePost(VALUE);
        s1.setBestTitlePost(VALUE);
        s1.setPlusOneMatrix(VALUE);
        s1.setSharedMatrix(VALUE);
        s1.setSumPlusOne(1l);
        s1.setSumShared(1l);
        s1.setTitleMatrix(VALUE);


        s2.setBestGoogleID(VALUE);
        s2.setBestNamePost(VALUE);
        s2.setBestTitlePost(VALUE);
        s2.setPlusOneMatrix(VALUE);
        s2.setSharedMatrix(VALUE);
        s2.setSumPlusOne(1l);
        s2.setSumShared(1l);
        s2.setTitleMatrix(VALUE);

        Assert.assertFalse(s1.equals(s2));
    }
}

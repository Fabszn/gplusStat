package models.domain;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * .
 *
 * @author fsznajderman
 *         date :  07/07/12
 */
public class ArticleTest extends TestCase {

    public static final String AZE = "aze";

    public void testEqualsOk() throws Exception {

        final Article a1 = new Article(AZE, "a", AZE, 1l, 1l, 1l, AZE, 1l, AZE);
        final Article a2 = new Article("", "", AZE, 1l, 1l, 1l, AZE, 1l, AZE);

        Assert.assertTrue(a1.equals(a2));


    }
    public void testEqualsFail() throws Exception {

        final Article a1 = new Article(AZE, AZE, AZE, 2l, 1l, 1l, AZE, 1l, AZE);
        final Article a2 = new Article("", AZE, AZE, 1l, 3l, 1l, AZE, 2l, AZE);

        Assert.assertFalse(a1.equals(a2));


    }
}

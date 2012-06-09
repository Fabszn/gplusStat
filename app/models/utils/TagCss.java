package models.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * .
 *
 * @author fsznajderman
 *         date :  16/06/12
 */
public enum TagCss {

    SMALL(2,4,"small"),MEDIUM(5,9,"medium"),LARGE(10,500,"large");


    private int min,max;
    private String lbl;
    private static final  List<TagCss> tCss =Arrays.asList(TagCss.values());

    TagCss(final int min,final int max,final String lbl){
        this.min=min;
        this.max=max;
        this.lbl=lbl;

    }

    public static String getClass(final int nb){

        Collection<TagCss> result = Collections2.filter(tCss,new Predicate<TagCss>() {
            public boolean apply(@Nullable TagCss tagCss) {
                System.out.println((tagCss.max>=nb && nb>=tagCss.min) + "  " + nb);
                return tagCss.max>=nb && nb>=tagCss.min;
            }
        });
             System.out.println("sqsdsf " + result.size());
        return result.iterator().next().lbl;
    }

}

package models.utils;

import com.google.common.base.Preconditions;
import play.Play;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * .
 *
 * @author fsznajderman
 *         date :  03/06/12
 */
public class ConfUtil {

      public static final String PATH_GOOGLE_KEY_PROPERTIES = "google.info.path";

     public static String getValueFromGplusConf(final String key) {
        String path = Play.configuration.getProperty(PATH_GOOGLE_KEY_PROPERTIES);

        Preconditions.checkNotNull(path);
        final Properties p = new Properties();
        try {
            p.load(new FileInputStream(new File(path)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p.getProperty(key);

    }
}

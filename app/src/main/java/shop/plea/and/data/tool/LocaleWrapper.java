package shop.plea.and.data.tool;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

/**
 * Created by kwonchulho on 2018. 1. 15..
 */

public class LocaleWrapper {
    private static Locale sLocale = null;

    public static Context wrap(Context base) {
        if (sLocale == null) {
            return base;
        }

        final Resources res = base.getResources();
        final Configuration config = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(sLocale);
            return base.createConfigurationContext(config);
        }
        else
        {
            base.getResources().updateConfiguration(config, base.getResources().getDisplayMetrics());
            return base;
        }

    }

    public static void setLocale(String lang) {
        sLocale = new Locale(lang);
    }

}

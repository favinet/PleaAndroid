package shop.plea.and.data.tool;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

import shop.plea.and.common.tool.Logger;

/**
 * Created by kwonchulho on 2018. 1. 14..
 */

public class LocaleChage extends ContextWrapper {
    public LocaleChage(Context base) {
        super(base);
    }

    @SuppressWarnings("deprecation")
    public static ContextWrapper wrap(Context context, String language) {
        Configuration config = context.getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }
        if (!language.equals("") && !sysLocale.getLanguage().equals(language)) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setSystemLocale(config, locale);
            } else {
                setSystemLocaleLegacy(config, locale);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //Logger.log(Logger.LogState.E, "누겟 CHANGE locale = " + locale);
                context = context.createConfigurationContext(config);
            } else {
                //Logger.log(Logger.LogState.E, "CHANGE locale = " + locale);
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }
        }
        return new LocaleChage(context);
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config){
        return config.locale;
    }

    @SuppressWarnings("deprecation")
    public static void setSystemLocaleLegacy(Configuration config, Locale locale){
        config.locale = locale;
        config.setLocale(locale);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static void setSystemLocale(Configuration config, Locale locale){
        //Logger.log(Logger.LogState.E, "누겟 setSystemLocale locale = " + locale);
        config.setLocale(locale);
    }
}
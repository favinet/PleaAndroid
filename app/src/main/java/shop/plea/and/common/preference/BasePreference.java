package shop.plea.and.common.preference;

/**
 * Created by shimtaewoo on 2017-10-02.
 */
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.lang.reflect.Type;


public class BasePreference {

    private static BasePreference instance;
    private final String PREF_NAME = "plea.shop.pref";
    public static final String ID = "id";
    public static final String AUTH_ID = "AUTH_ID";
    public static final String JOIN_TYPE = "JOIN_TYPE";
    public static final String USERINFO_DATA = "USERINFO_DATA";
    public static final String LOCALE = "LOCALE";
    public static final String GCM_TOKEN = "GCM_TOKEN";


    static Context mContext;
    private Gson mson;

    public BasePreference(Context context)
    {
        mContext = context;
        mson = new Gson();
    }

    public static BasePreference getInstance(Context context) {

        if(instance == null)
        {
            instance = new BasePreference(context.getApplicationContext());
        }
        return instance;
    }

    public void put(String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, boolean value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public void put(String key, int value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    public void putObject(String key, Object object) {
        if (object == null) throw new IllegalArgumentException("Object is null");
        if (key.equals("") || key == null) throw new IllegalArgumentException("Key is empty or null");

        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, mson.toJson(object));
        editor.commit();
    }

    public <T> T getObject(String key, Type a) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        String gson = pref.getString(key, null);

        if (gson == null) {
            return null;
        }
        else {
            try {
                return mson.fromJson(gson, a);
            }
            catch (Exception e) {
                throw new IllegalArgumentException("Object stored with key " + key + " is instance of other class");
            }
        }
    }

    public String getValue(String key, String dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);

        try {
            return pref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }

    public int getValue(String key, int dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);

        try {
            return pref.getInt(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }

    public boolean getValue(String key, boolean dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);

        try {
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public void remove(String key){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.remove(key);
        editor.commit();
    }

    public void removeAll(){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.clear();
        editor.commit();
    }
}


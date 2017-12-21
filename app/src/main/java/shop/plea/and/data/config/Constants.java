package shop.plea.and.data.config;

/**
 * Created by shimtaewoo on 2017-10-02.
 */

public class Constants {
    public static final boolean IS_DEV = false;

    public static final String REAL_DOMAIN = "plea.shop";
    public static final String TEST_DOMAIN = "plea.shop";
    public static final String DOING_DOMAIN = IS_DEV ? TEST_DOMAIN : REAL_DOMAIN;
    public static final String BASE_URL = "http://" + Constants.DOING_DOMAIN;
    public static final String MAIN_URL = "http://" + Constants.DOING_DOMAIN + "/main?uid=%s";

    public static final String INTENT_ACTION_SAVE_COMPLETE = "INTENT_ACTION_SAVE_COMPLETE";

    public static final String LOG_TAG = "PLEA";
    public static final String INTENT_DATA_KEY = "INTENT_DATA_KEY";

    public static final String API_SUCCESS = "success";
    public static final String API_FAIL = "fail";

    public final class MENU_LINKS{
        public static final String PROFILE = "http://" + Constants.DOING_DOMAIN + "/member/memberView/%s?uid=%s";
        public static final String MY_PLEA = "http://" + Constants.DOING_DOMAIN + "/main/MY?uid=%s";
        public static final String FREIEND_NEWS = "http://" + Constants.DOING_DOMAIN + "/main/FOLLOWING?uid=%s";
        public static final String RECOMMEND_PLEA = "http://" + Constants.DOING_DOMAIN + "/main/RECOMMEND?uid=%s";
        public static final String NOTICE = "http://" + Constants.DOING_DOMAIN + "/notice?uid=%s";
        public static final String TERMS = "http://" + Constants.DOING_DOMAIN + "/join/terms?locale=%s";
        public static final String POLICY = "http://" + Constants.DOING_DOMAIN + "/join/policy?locale=%s";
        public static final String RESET_PASSWORD = "http://" + Constants.DOING_DOMAIN + "/member/password/passwordReset?uid=%s";
        public static final String BLOCK = "http://" + Constants.DOING_DOMAIN + "/block/%s";
        public static final String SEARCH_MAIN = "http://" + Constants.DOING_DOMAIN + "/search?uid=%s";
        public static final String SEARCH_RESULT = "http://" + Constants.DOING_DOMAIN + "/search/tag/%s?uid=%s";
        public static final String PUSH = "http://" + Constants.DOING_DOMAIN + "/member/pushSetting?uid=%s";
        public static final String EMAIL_RECEIVE = "http://" + Constants.DOING_DOMAIN + "/member/mailSetting?uid=%s";
        public static final String PLEA = "http://" + Constants.DOING_DOMAIN + "/cart/reg?txtUrl=%s&uid=%s ";

    }


    public final class VIEW_ANIMATION {
        public static final int ANI_NONE = 10;
        public static final int ANI_FADE = 11;
        public static final int ANI_FLIP = 12;
        public static final int ANI_END_ENTER = 13;
        public static final int ANI_SLIDE_DOWN_IN = 14;
        public static final int ANI_SLIDE_LEFT_IN = 15;
        public static final int ANI_SLIDE_RIGHT_IN = 16;
    }

    public final class PREF_KEY {
        public static final String EVENT_USER_IDX = "EVENT_USER_IDX";
        public static final String EVENT_EVENT_IDX = "EVENT_EVENT_IDX";
        public static final String EVENT_TDNICK = "EVENT_TDNICK";
        public static final String EVENT_SAVING = "EVENT_SAVING";
    }

    public final class FRAGMENT_MENUID {
        public static final int SINGUP = 0;
        public static final int LOGIN = 1;
        public static final int FIND_PASSWORD = 2;
        public static final int SIGN_INFO = 3;
        public static final int SIGN_EMAIL = 4;
        public static final int RESET_PASSWORD = 5;
    }

    public final class API_PARAMS_KEYS {
        public static final String JOIN_TYPE = "joinType";
        public static final String EMAIL = "email";
        public static final String NICKNAME = "nickname";
        public static final String PASSWORD = "password";
        public static final String AUTHID = "authId";
        public static final String FILE = "file";
        public static final String PROFILE_IMG = "profileImg";
        public static final String SNS_EMAIL = "snsEmail";
        public static final String ID = "id";
        public static final String LOCALE = "locale";
        public static final String GCM_TOKEN = "deviceToken";
        public static final String DEVICE_TYPE = "deviceType";
        public static final String GENDER = "gender";
        public static final String BIRTH = "birth";


    }

    public final class LOGIN_TYPE {
        public static final String EMAIL = "email";
        public static final String GOOGLE = "google";
        public static final String FACEBOOK = "facebook";

    }

    public final class SCREEN_TYPE {
        public static final String INAPP = "inapp";
        public static final String SHOP = "shop";
        public static final String PUSH = "push";

    }
}

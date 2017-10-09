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

    public static final String LOG_TAG = "PLEA";

    public static final String INTENT_DATA_KEY = "INTENT_DATA_KEY";

    public static final String API_SUCCESS = "success";
    public static final String API_FAIL = "fail";

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

}

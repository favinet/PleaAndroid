package shop.plea.and.common.tool;

import com.google.gson.Gson;

/**
 * Created by kwon7575 on 2017-10-13.
 */

public class Utils {

    /**
     * Object convert to json String
     * @param obj
     * @return
     */
    public static String getStringByObject(Object obj)
    {
        Gson gson = new Gson();
        String json = gson.toJson(obj);

        return json;
    }

}

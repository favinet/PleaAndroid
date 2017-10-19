package shop.plea.and.common.tool;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 이메일 포맷 체크
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){

        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }

}

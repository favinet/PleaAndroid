package shop.plea.and.data.model;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.preference.BasePreference;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.tool.DataInterface;
import shop.plea.and.data.tool.DataManager;

/**
 * Created by kwon7575 on 2017-10-17.
 */

public class UserInfo {

    private static UserInfo instance;
    private  UserInfoData userInfoData = null;
    private HashMap<String, String> loginParams = new HashMap<>();

    public static UserInfo getInstance() {
        if(instance == null)
        {
            instance = new UserInfo();
        }

        return instance;
    }

    public void setCurrentUserInfoData(Context context, UserInfoData userInfoData)
    {
        this.userInfoData = userInfoData;
        BasePreference.getInstance(context).putObject(BasePreference.USERINFO_DATA, userInfoData);
    }

    public UserInfoData getCurrentUserInfoData(Context context)
    {
        if(userInfoData == null)
        {
            userInfoData = BasePreference.getInstance(context).getObject(BasePreference.USERINFO_DATA, UserInfoData.class);
            if(userInfoData == null)
            {
                userInfoData = new UserInfoData();
            }
        }

        return userInfoData;
    }

    public void setParams(String key, String value)
    {
        if(loginParams.containsKey(key))
        {
            loginParams.remove(key);
        }

        loginParams.put(key, value);
    }

    public void addParams(HashMap<String, String> params)
    {
        loginParams.putAll(params);
    }

    public HashMap<String, String> getLoginParams()
    {
        return loginParams;
    }

    public void clearParams()
    {
        loginParams.clear();
    }

}

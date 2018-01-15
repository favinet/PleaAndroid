package shop.plea.and.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kwon7575 on 2017-10-17.
 */

public class UserInfoResultData extends ResponseData {


    @SerializedName("userData")
    @Expose
    public UserInfoData userData;

    public UserInfoResultData(){
        userData = new UserInfoData();
    }

}

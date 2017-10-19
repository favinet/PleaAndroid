package shop.plea.and.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwon7575 on 2017-10-17.
 */

public class UserInfoResultData extends ResponseData {

    public UserInfoData userData;

    public UserInfoResultData(){
        userData = new UserInfoData();
    }

}

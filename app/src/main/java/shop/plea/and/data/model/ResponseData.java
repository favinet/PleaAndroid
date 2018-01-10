package shop.plea.and.data.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by kwon7575 on 2017-10-17.
 */

public class ResponseData {

    @SerializedName("result")
    @Expose
    public String result = "";

    @SerializedName("flag")
    @Expose
    public boolean flag = false;

    @SerializedName("count")
    @Expose
    public int count = 0;

    @SerializedName("message")
    @Expose
    public String message = "";

    @SerializedName("profileImg")
    @Expose
    public String profileImg = "";

    @SerializedName("data")
    @Expose
    public VerSionData data = new VerSionData();

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public VerSionData getData() {
        return data;
    }

    public void setData(VerSionData data) {
        this.data = data;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}

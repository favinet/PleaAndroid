package shop.plea.and.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    @SerializedName("message")
    @Expose
    public String message = "";

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
}

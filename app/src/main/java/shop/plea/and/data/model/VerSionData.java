package shop.plea.and.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kwon on 2018-01-10.
 */

public class VerSionData {

    @SerializedName("iosVersion")
    @Expose
    private String iosVersion;
    @SerializedName("androidVersion")
    @Expose
    private String androidVersion;

    public String getIosVersion() {
        return iosVersion;
    }

    public void setIosVersion(String iosVersion) {
        this.iosVersion = iosVersion;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

}

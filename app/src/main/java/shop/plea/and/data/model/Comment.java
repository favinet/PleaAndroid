package shop.plea.and.data.model;

/**
 * Created by master on 2017-10-02.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("userId")
    @Expose
    private UserInfoData userId;
    @SerializedName("contentId")
    @Expose
    private String contentId;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("delYN")
    @Expose
    private String delYN;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserInfoData getUserId() {
        return userId;
    }

    public void setUserId(UserInfoData userId) {
        this.userId = userId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDelYN() {
        return delYN;
    }

    public void setDelYN(String delYN) {
        this.delYN = delYN;
    }

}

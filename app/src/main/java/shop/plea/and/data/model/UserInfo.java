package shop.plea.and.data.model;

/**
 * Created by master on 2017-10-02.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("joinType")
    @Expose
    private String joinType;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("snsEmail")
    @Expose
    private String snsEmail;
    @SerializedName("authId")
    @Expose
    private String authId;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("profileImg")
    @Expose
    private String profileImg;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("deleteDate")
    @Expose
    private Object deleteDate;
    @SerializedName("lastLogin")
    @Expose
    private String lastLogin;
    @SerializedName("memo")
    @Expose
    private String memo;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("following")
    @Expose
    private List<String> following = null;
    @SerializedName("follow")
    @Expose
    private List<String> follow = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSnsEmail() {
        return snsEmail;
    }

    public void setSnsEmail(String snsEmail) {
        this.snsEmail = snsEmail;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Object deleteDate) {
        this.deleteDate = deleteDate;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public List<String> getFollow() {
        return follow;
    }

    public void setFollow(List<String> follow) {
        this.follow = follow;
    }

}

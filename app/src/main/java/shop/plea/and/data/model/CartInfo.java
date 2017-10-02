package shop.plea.and.data.model;

/**
 * Created by master on 2017-10-02.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartInfo {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("authType")
    @Expose
    private String authType;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("shopUrl")
    @Expose
    private String shopUrl;
    @SerializedName("userId")
    @Expose
    private UserInfo userId;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("delYN")
    @Expose
    private String delYN;
    @SerializedName("dislike")
    @Expose
    private List<Object> dislike = null;
    @SerializedName("like")
    @Expose
    private List<Object> like = null;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("imgUrl")
    @Expose
    private List<String> imgUrl = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public UserInfo getUserId() {
        return userId;
    }

    public void setUserId(UserInfo userId) {
        this.userId = userId;
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

    public List<Object> getDislike() {
        return dislike;
    }

    public void setDislike(List<Object> dislike) {
        this.dislike = dislike;
    }

    public List<Object> getLike() {
        return like;
    }

    public void setLike(List<Object> like) {
        this.like = like;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        this.imgUrl = imgUrl;
    }

}

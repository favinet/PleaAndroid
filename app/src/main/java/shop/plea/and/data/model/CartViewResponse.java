package shop.plea.and.data.model;

/**
 * Created by master on 2017-10-02.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CartViewResponse {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("cartInfo")
    @Expose
    private CartInfo cartInfo;
    @SerializedName("commentList")
    @Expose
    private List<Comment> commentList = null;
    @SerializedName("commentUserList")
    @Expose
    private List<UserInfoData> commentUserList = null;
    @SerializedName("paging")
    @Expose
    private Paging paging;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public CartInfo getCartInfo() {
        return cartInfo;
    }

    public void setCartInfo(CartInfo cartInfo) {
        this.cartInfo = cartInfo;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<UserInfoData> getCommentUserList() {
        return commentUserList;
    }

    public void setCommentUserList(List<UserInfoData> commentUserList) {
        this.commentUserList = commentUserList;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}

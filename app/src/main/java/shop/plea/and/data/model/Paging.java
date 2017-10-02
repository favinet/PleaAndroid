package shop.plea.and.data.model;

/**
 * Created by master on 2017-10-02.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Paging {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("current")
    @Expose
    private Integer current;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

}
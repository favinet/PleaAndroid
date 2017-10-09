package shop.plea.and.data.service;

/**
 * Created by master on 2017-10-02.
 */

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import shop.plea.and.data.model.CartViewResponse;

public interface HttpService {

    @GET("/cart/api/cartView/{id}?dev=1")
    Call<CartViewResponse> callCartView(@Path("id") String cartid);

}

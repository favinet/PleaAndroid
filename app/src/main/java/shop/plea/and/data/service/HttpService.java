package shop.plea.and.data.service;

/**
 * Created by master on 2017-10-02.
 */

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import shop.plea.and.data.model.ResponseData;
import shop.plea.and.data.model.UserInfoResultData;

public interface HttpService {

    @Multipart
    @POST("/join/api/userReg")
    Call<UserInfoResultData> callUserRegist(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("/api/login")
    Call<UserInfoResultData> callUserLogin(@FieldMap Map<String, String> params);

    @GET("/join/api/checkEmail")
    Call<ResponseData> callUserEmailCheck(@Query("email") String email);

    @GET("/join/api/checkNickname")
    Call<ResponseData> callUserNickNameCheck(@Query("nickname") String nickname);

}

package shop.plea.and.data.service;

/**
 * Created by master on 2017-10-02.
 */

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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
    Call<ResponseData> callUserEmailCheck(@Query("email") String email, @Query("locale") String locale);

    @GET("/join/api/checkNickname")
    Call<ResponseData> callUserNickNameCheck(@Query("nickname") String nickname, @Query("locale") String locale);

    @FormUrlEncoded
    @POST("/join/api/password/sendMail")
    Call<ResponseData> callSendPasswordMail(@Field("email") String email, @Field("locale") String locale);

    @FormUrlEncoded
    @POST("/member/api/modify/{id}")
    Call<UserInfoResultData> callUserUpdate(@Path("id") String id, @FieldMap Map<String, String> params);

    @GET("/notice/api/noticeCnt/{id}")
    Call<ResponseData> callGetNoticeCnt(@Path("id") String id);

    @FormUrlEncoded
    @POST("/member/api/delete/{id}")
    Call<ResponseData> callUserDelete(@Path("id") String id, @FieldMap Map<String, String> params);

    @GET("/common/api/appVersion")
    Call<ResponseData> callGetVersion();

    @Multipart
    @POST("/member/api/imgUpload")
    Call<ResponseData> callUploadImg(@Query("uid") String uid, @Part MultipartBody.Part file);
}

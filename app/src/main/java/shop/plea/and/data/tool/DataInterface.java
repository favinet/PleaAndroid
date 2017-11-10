package shop.plea.and.data.tool;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.model.CartViewResponse;
import shop.plea.and.data.model.RequestData;
import shop.plea.and.data.model.ResponseData;
import shop.plea.and.data.model.UserInfoResultData;

/**
 * Created by master on 2017-10-02.
 */

public class DataInterface extends BaseDataInterface{

    private static DataInterface instance;

    public interface ResponseCallback<T> {
        void onSuccess(T response);
        void onError();

    }

    public static DataInterface getInstance() {
        if (instance == null) {
            synchronized (DataInterface.class) {
                if (instance == null) {
                    instance = new DataInterface();
                }
            }
        }

        return instance;
    }

    public DataInterface() {
        super();
    }

    public static boolean isCallSuccess(Response response) {
        return response.isSuccessful();
    }

    public void callCartView(Context context, String cartid, final ResponseCallback callback)
    {
        try {
            Call<CartViewResponse> call = service.callCartView(cartid);

            call.enqueue(new RetryableCallback<CartViewResponse>(call, context) {
                @Override
                public void onFinalResponse(Call<CartViewResponse> call, retrofit2.Response<CartViewResponse> response) {
                    if (callback == null) return;

                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        Logger.log(Logger.LogState.E, "error callCartView = " + response.errorBody().toString());
                        callback.onError();
                    }
                }

                @Override
                public void onFinalFailure(Call<CartViewResponse> call, Throwable t) {
                    if (callback == null)
                        return;
                    t.printStackTrace();
                    callback.onError();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            callback.onError();
        }
    }

    public void userRegist(Context context, Map<String, RequestBody> params, File file, final ResponseCallback callback)
    {
        try {

            RequestBody body = (file == null) ? null : RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = (file == null) ? null : MultipartBody.Part.createFormData("Filedata", file.getName(), body);

            Call<UserInfoResultData> call = service.callUserRegist(params, filePart);

            call.enqueue(new RetryableCallback<UserInfoResultData>(call, context) {
                @Override
                public void onFinalResponse(Call<UserInfoResultData> call, retrofit2.Response<UserInfoResultData> response) {
                    if (callback == null) return;

                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        Logger.log(Logger.LogState.E, "error callUserRegist = " + response.errorBody().toString());
                        callback.onError();
                    }
                }

                @Override
                public void onFinalFailure(Call<UserInfoResultData> call, Throwable t) {
                    if (callback == null)
                        return;
                    t.printStackTrace();
                    callback.onError();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            callback.onError();
        }
    }

    public void userLogin(Context context, HashMap<String, String> params, final ResponseCallback callback)
    {
        try {
            Call<UserInfoResultData> call = service.callUserLogin(params);
            Logger.log(Logger.LogState.E, "error callUserLogin = " + Utils.getStringByObject(params));
            call.enqueue(new RetryableCallback<UserInfoResultData>(call, context) {
                @Override
                public void onFinalResponse(Call<UserInfoResultData> call, retrofit2.Response<UserInfoResultData> response) {
                    if (callback == null) return;

                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        Logger.log(Logger.LogState.E, "error callUserLogin = " + response.errorBody().toString());
                        callback.onError();
                    }
                }

                @Override
                public void onFinalFailure(Call<UserInfoResultData> call, Throwable t) {
                    if (callback == null)
                        return;
                    t.printStackTrace();
                    callback.onError();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            callback.onError();
        }
    }

    public void userEmailCheck(Context context, String email, final ResponseCallback callback)
    {
        try {
            Call<ResponseData> call = service.callUserEmailCheck(email);

            call.enqueue(new RetryableCallback<ResponseData>(call, context) {
                @Override
                public void onFinalResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                    if (callback == null) return;

                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        Logger.log(Logger.LogState.E, "error userEmailCheck = " + response.errorBody().toString());
                        callback.onError();
                    }
                }

                @Override
                public void onFinalFailure(Call<ResponseData> call, Throwable t) {
                    if (callback == null)
                        return;
                    t.printStackTrace();
                    callback.onError();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            callback.onError();
        }
    }

    public void userNickNameCheck(Context context, String nickname, final ResponseCallback callback)
    {
        try {
            Call<ResponseData> call = service.callUserNickNameCheck(nickname);

            call.enqueue(new RetryableCallback<ResponseData>(call, context) {
                @Override
                public void onFinalResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                    if (callback == null) return;

                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        Logger.log(Logger.LogState.E, "error userNickNameCheck = " + response.errorBody().toString());
                        callback.onError();
                    }
                }

                @Override
                public void onFinalFailure(Call<ResponseData> call, Throwable t) {
                    if (callback == null)
                        return;
                    t.printStackTrace();
                    callback.onError();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            callback.onError();
        }
    }
}

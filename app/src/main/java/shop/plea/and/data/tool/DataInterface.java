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
import shop.plea.and.common.preference.BasePreference;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
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
            call.enqueue(new RetryableCallback<UserInfoResultData>(call, context) {
                @Override
                public void onFinalResponse(Call<UserInfoResultData> call, retrofit2.Response<UserInfoResultData> response) {
                    if (callback == null) return;
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
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

    public void userEmailCheck(Context context, String email, String locale, final ResponseCallback callback)
    {
        try {
            Call<ResponseData> call = service.callUserEmailCheck(email, locale);

            call.enqueue(new RetryableCallback<ResponseData>(call, context) {
                @Override
                public void onFinalResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                    if (callback == null) return;

                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        Logger.log(Logger.LogState.D, "error userEmailCheck = " + response.errorBody().toString());
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

    public void userSendPasswordMail(Context context, String email, String locale, final ResponseCallback callback)
    {
        try
        {
            Call<ResponseData> call = service.callSendPasswordMail(email, locale);

            call.enqueue(new RetryableCallback<ResponseData>(call, context) {
                @Override
                public void onFinalResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                    if (callback == null) return;

                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
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

    public void userNickNameCheck(Context context, String nickname, String locale, final ResponseCallback callback)
    {
        try {
            Call<ResponseData> call = service.callUserNickNameCheck(nickname, locale);

            call.enqueue(new RetryableCallback<ResponseData>(call, context) {
                @Override
                public void onFinalResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                    if (callback == null) return;

                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
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

    public void userUpdate(Context context, String id, HashMap<String, String> params, final ResponseCallback callback)
    {

        try {

            Call<UserInfoResultData> call = service.callUserUpdate(id, params);

            call.enqueue(new RetryableCallback<UserInfoResultData>(call, context) {
                @Override
                public void onFinalResponse(Call<UserInfoResultData> call, retrofit2.Response<UserInfoResultData> response) {
                    if (callback == null) return;

                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
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

    public void userDelete(Context context, String id, HashMap<String, String> params, final ResponseCallback callback)
    {
        try {
            Call<ResponseData> call = service.callUserDelete(id, params);

            call.enqueue(new RetryableCallback<ResponseData>(call, context) {
                @Override
                public void onFinalResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                    if (callback == null) return;

                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        Logger.log(Logger.LogState.D, "error userDelete = " + response.errorBody().toString());
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

    public void getNoticeCnt(Context context, String id, final ResponseCallback callback)
    {
        try {
            Call<ResponseData> call = service.callGetNoticeCnt(id);

            call.enqueue(new RetryableCallback<ResponseData>(call, context) {
                @Override
                public void onFinalResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                    if (callback == null) return;

                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        Logger.log(Logger.LogState.D, "error userNoticeCnt = " + response.errorBody().toString());
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

    public void getVersion(Context context, final ResponseCallback callback)
    {
        try {
            Call<ResponseData> call = service.callGetVersion();

            call.enqueue(new RetryableCallback<ResponseData>(call, context) {
                @Override
                public void onFinalResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                    if (callback == null) return;

                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        Logger.log(Logger.LogState.D, "error getVersion = " + response.errorBody().toString());
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


    public void uploadProfile(Context context, String uid, File file, final ResponseCallback callback)
    {
        try {

            RequestBody body = (file == null) ? null : RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = (file == null) ? null : MultipartBody.Part.createFormData("Filedata", file.getName(), body);

            Call<ResponseData> call = service.callUploadImg(uid,filePart);

            call.enqueue(new RetryableCallback<ResponseData>(call, context) {
                @Override
                public void onFinalResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                    if (callback == null) return;

                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        Logger.log(Logger.LogState.D, "error callUserRegist = " + response.errorBody().toString());
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

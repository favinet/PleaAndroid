package shop.plea.and.data.tool;

import android.content.Context;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.data.model.CartViewResponse;
import shop.plea.and.data.model.RequestData;
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

    public void userRegist(Context context, HashMap<String, String> params, final ResponseCallback callback)
    {
        try {
            Call<UserInfoResultData> call = service.callUserRegist(params);

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
}

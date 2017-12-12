package shop.plea.and.data.tool;

/**
 * Created by master on 2017-10-02.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.tool.Logger;

public abstract class RetryableCallback<T> implements Callback<T> {

    private int totalRetries = 3;
    private static final String TAG = RetryableCallback.class.getSimpleName();
    private final Call<T> call;
    private int retryCount = 0;
    private Context context;

    public RetryableCallback(Call<T> call, Context context) {
        this.call = call;
        this.context = context;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!DataInterface.isCallSuccess(response) && (context instanceof Activity))
        {
            retryPopup();
        }
//            if (retryCount++ < totalRetries) {
//                Log.v(TAG, "Retrying API Call -  (" + retryCount + " / " + totalRetries + ")");
//                retry();
//            } else
//                onFinalResponse(call, response);
        else
            onFinalResponse(call,response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
//        Log.e(TAG, t.getMessage());
        if (context instanceof Activity) retryPopup();
        else onFinalFailure(call, t);

//        if (retryCount++ < totalRetries) {
//            Log.v(TAG, "Retrying API Call -  (" + retryCount + " / " + totalRetries + ")");
//            retry();
//        } else
//            onFinalFailure(call, t);
    }

    public void onFinalResponse(Call<T> call, Response<T> response) {

    }

    public void onFinalFailure(Call<T> call, Throwable t) {
    }

    private void retry() {
        call.clone().enqueue(this);
    }

    private void retryPopup()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("플리").setMessage("데이터 요청에 실패하였습니다.\n사용중인 네트워크 상태를 확인해 주세요.\n다시 요청하시겠습니까?").setCancelable(false).setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                retry();
            }
        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Logger.log(Logger.LogState.E, "BaseActivity ?? " +  (context instanceof BaseActivity));
                if(context instanceof BaseActivity)
                {
                    ((BaseActivity)context).stopIndicator();
                }
//                ((Activity)context).moveTaskToBack(true);
//                ((Activity)context).finish();
//                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }).create().setCanceledOnTouchOutside(false);

        if(context != null && !((Activity)context).isFinishing()) dialog.show();
    }
}


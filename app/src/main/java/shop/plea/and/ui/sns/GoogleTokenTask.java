package shop.plea.and.ui.sns;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import shop.plea.and.common.tool.Logger;
import shop.plea.and.data.model.UserInfo;

/**
 * Created by kwon7575 on 2017-10-18.
 */

public class GoogleTokenTask extends AsyncTask<String, Void, String> {

    private Context mContext;
    private GoogleSignInAccount acct;


    public GoogleTokenTask(Context context, GoogleSignInAccount acct)
            {
            this.mContext = context;
            this.acct = acct;
    }

    @Override
    protected void onPreExecute() {
            super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String token = "";

        try
        {
            String url = params[0];
            token = GoogleAuthUtil.getToken(mContext, acct.getEmail(), url);
            Logger.log(Logger.LogState.D, "google token  : " + token);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return token;
    }

    @Override
    protected void onPostExecute(String token) {
        super.onPostExecute(token);

        UserInfo.getInstance().setParams("google", token);
    }
}

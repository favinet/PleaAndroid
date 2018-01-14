package shop.plea.and.ui.sns;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.util.HashMap;

import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.preference.BasePreference;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.RequestData;
import shop.plea.and.data.model.UserInfo;
import shop.plea.and.data.model.UserInfoData;
import shop.plea.and.data.model.UserInfoResultData;
import shop.plea.and.data.parcel.IntentData;
import shop.plea.and.data.tool.DataInterface;
import shop.plea.and.data.tool.DataManager;
import shop.plea.and.ui.activity.MainPleaListActivity;
import shop.plea.and.ui.fragment.SignUpInfoFragment;

/**
 * Created by kwon7575 on 2017-10-17.
 */

public class SNSHelper {

    private CallbackManager facebookCallBackManager;
    private LoginButton facebookLogin;
    private ProfileTracker FBprofileTracker;
    private BaseActivity base;

    private GoogleApiClient mGoogleApiClient;
    private GoogleConnectionListener mGoogleConnectionListener = new GoogleConnectionListener();
    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private boolean isLogin = false;

    public SNSHelper(BaseActivity activity, LoginButton facebook, boolean login){
        this.isLogin = login;
        this.base = activity;
        setButton(facebook);
        snsInit();
    }

    private void setButton(LoginButton facebook)
    {
        facebookLogin = facebook;
    }

    private void snsInit()
    {
        facebookCallBackManager = CallbackManager.Factory.create();
        facebookLogin.registerCallback(facebookCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                final Profile profile = Profile.getCurrentProfile();

                if(profile == null)
                {
                    FBprofileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            updateFbProfile();
                            FBprofileTracker.stopTracking();
                        }
                    };
                }
                else
                    updateFbProfile();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(base);
                dialog.setTitle(R.string.app_name).setMessage(Utils.getStringByObject(error)).setPositiveButton(base.getString(R.string.yes), null).create().show();
            }
        });

        // google init
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(base.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(base)
                .enableAutoManage((FragmentActivity) base, mGoogleConnectionListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void updateFbProfile() {
        final Profile profile = Profile.getCurrentProfile();
        UserInfo.getInstance().clearParams();
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        String token = BasePreference.getInstance(base).getValue(BasePreference.GCM_TOKEN, "");
                        String locale = BasePreference.getInstance(base).getValue(BasePreference.LOCALE, null);

                        RequestData req = new RequestData();
                        req.authId = profile.getId();
                        req.joinType = "facebook";

                        Uri profileImg = profile.getProfilePictureUri(100, 100);

                        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.AUTHID, req.authId);
                        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.JOIN_TYPE, req.joinType);
                        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.DEVICE_TYPE, "android");
                        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.GCM_TOKEN, token);
                        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.LOCALE, locale);

                        if(profileImg != null)
                            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.PROFILE_IMG, profileImg.toString());

                        try
                        {
                            if(object.has("email"))
                            {
                                if(Utils.checkEmail(object.getString("email")))
                                {
                                    UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.SNS_EMAIL, object.getString("email"));
                                }
                            }

                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }

                        userCheck(UserInfo.getInstance().getLoginParams());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void userCheck(HashMap<String, String> params)
    {
        userLogin(params);
    }

    private void userLogin(final HashMap<String, String> params)
    {
        base.startIndicator("");

        DataManager.getInstance(base).api.userLogin(base, params, new DataInterface.ResponseCallback<UserInfoResultData>() {
            @Override
            public void onSuccess(UserInfoResultData response) {
                base.stopIndicator();

                String result = response.getResult();
                if(result.equals(Constants.API_FAIL))
                {
                    if(response.getMessage().contains("Unregistered email") || response.getMessage().contains("등록되지 않은"))
                    {
                        nextScreen(params);
                    }
                    else
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(base);
                        dialog.setTitle(R.string.app_name).setMessage(response.getMessage()).setPositiveButton(base.getString(R.string.yes), null).create().show();
                    }
                }
                else
                {
                    UserInfoData userInfoData = response.userData;
                    UserInfo.getInstance().setCurrentUserInfoData(base, userInfoData);
                    BasePreference.getInstance(base).put(BasePreference.ID, userInfoData.getId());
                    BasePreference.getInstance(base).put(BasePreference.JOIN_TYPE, userInfoData.getJoinType());
                    BasePreference.getInstance(base).put(BasePreference.AUTH_ID, userInfoData.getAuthId());
                    BasePreference.getInstance(base).put(BasePreference.LOCALE, userInfoData.getLocale());
                    BasePreference.getInstance(base).putObject(BasePreference.USERINFO_DATA, userInfoData);

                    IntentData indata = new IntentData();
                    indata.isRegist = false;
                    indata.link = String.format(Constants.MAIN_URL, userInfoData.getId());
                    Intent intent = new Intent(base, MainPleaListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(Constants.INTENT_DATA_KEY, indata);
                    base.startActivity(intent);
                    base.finish();
                }
            }

            @Override
            public void onError() {
                base.stopIndicator();
            }
        });

    }

    public void googleLogin()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        base.startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    public void facebookLogin()
    {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null)
        {
            LoginManager.getInstance().logOut();
        }

        facebookLogin.performClick();
    }

    public void removeSNSCallback()
    {
        if(FBprofileTracker != null)
        {
            FBprofileTracker.stopTracking();
        }
        if(mGoogleApiClient != null)
        {
            mGoogleApiClient.stopAutoManage(base);
            mGoogleApiClient.disconnect();
        }
    }

    private void nextScreen(HashMap<String, String> params)
    {
        SignUpInfoFragment signUpInfoFragment = new SignUpInfoFragment().newInstance(
                Constants.FRAGMENT_MENUID.SIGN_INFO,
                params
        );
        base.addFragment(signUpInfoFragment, Constants.FRAGMENT_MENUID.SIGN_INFO);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        UserInfo.getInstance().clearParams();
        if (facebookCallBackManager != null) {
            if (facebookCallBackManager.onActivityResult(requestCode, resultCode, data)) {
                return;
            }
        }

        if ( requestCode == RC_GOOGLE_SIGN_IN ) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if ( result.isSuccess() ) {
                GoogleSignInAccount account = result.getSignInAccount();

                String locale = BasePreference.getInstance(base).getValue(BasePreference.LOCALE, null);

                RequestData req = new RequestData();
                req.authId = account.getId();
                req.joinType = "google";

                Uri profileImg = account.getPhotoUrl();

                String gcmToken = BasePreference.getInstance(base).getValue(BasePreference.GCM_TOKEN, "");

                UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.AUTHID, req.authId);
                UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.JOIN_TYPE, req.joinType);
                UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.SNS_EMAIL, account.getEmail());
                UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.GCM_TOKEN, gcmToken);
                UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.DEVICE_TYPE, "android");
                UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.LOCALE, locale);

                if(profileImg != null)
                    UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.PROFILE_IMG, account.getPhotoUrl().toString());

                GoogleTokenTask task = new GoogleTokenTask(base, account);
                task.execute("oauth2:https://www.googleapis.com/auth/userinfo.profile");

                userCheck(UserInfo.getInstance().getLoginParams());
            }
            else
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(base);
                dialog.setTitle(R.string.app_name).setMessage(base.getString(R.string.google_error) + "\n" + Utils.getStringByObject(result)).setPositiveButton(base.getString(R.string.yes), null).create().show();
            }
        }
    }


    private class GoogleConnectionListener implements GoogleApiClient.OnConnectionFailedListener {

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Logger.log(Logger.LogState.E, "google onConnectionFailed" + connectionResult);
        }
    }
}

package shop.plea.and.ui.sns;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
import shop.plea.and.data.tool.DataInterface;
import shop.plea.and.data.tool.DataManager;

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

    public SNSHelper(BaseActivity activity, LoginButton facebook){
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
                Logger.log(Logger.LogState.E, "facebook login ok!!");

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

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        Logger.log(Logger.LogState.D, response.toString());

                        RequestData req = new RequestData();
                        req.authId = profile.getId();
                        req.joinType = "facebook";

                        Toast.makeText(base, "페이스북 로그인 성공!!" + profile.getId(), Toast.LENGTH_LONG).show();

                        Uri profileImg = profile.getProfilePictureUri(100, 100);

                        UserInfo.getInstance().setParams("authId", req.authId);
                        UserInfo.getInstance().setParams("joinType", req.joinType);
                        if(profileImg != null)
                            UserInfo.getInstance().setParams("profileImg", profileImg.toString());
                        //UserInfo.getInstance().setParams(BasePreference.FACEBOOK_TOKEN, AccessToken.getCurrentAccessToken().getToken());

                        try {
                            if(object.has("email"))
                            {
                                if(Utils.checkEmail(object.getString("email")))
                                {
                                    UserInfo.getInstance().setParams("snsEmail", object.getString("email"));
                                }
                            }
                            if(object.has("name")) UserInfo.getInstance().setParams("nickname", object.getString("name"));
//							if(object.has("gender")) req.gender = object.getString("gender").equals("male") ? "M" : "F";
//							if(object.has("birthday")) req.birthday = object.getString("birthday");

                            userRegist(UserInfo.getInstance().getLoginParams());
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void userRegist(HashMap<String, String> params)
    {
        Logger.log(Logger.LogState.W, "userRegist : " + Utils.getStringByObject(params));
        base.startIndicator("");
        DataManager.getInstance(base).api.userRegist(base, params, new DataInterface.ResponseCallback<UserInfoResultData>() {
            @Override
            public void onSuccess(UserInfoResultData response) {
                Log.e("userRegist onSuccess : ", ""+Utils.getStringByObject(response));

                if(response.getResult().equals(Constants.API_FAIL))
                {
                    Toast.makeText(base, "회원가입 실패!!" + response.getMessage(), Toast.LENGTH_LONG).show();
                    UserInfoData userInfoData = BasePreference.getInstance(base).getObject(BasePreference.USERINFO_DATA, UserInfoData.class);
                    Log.e("userRegist userInfoData : ", ""+Utils.getStringByObject(userInfoData));
                    if(userInfoData != null)
                        userLogin(userInfoData);

                }
                else
                {
                    Toast.makeText(base, "회원가입 성공!!" + response.getResult(), Toast.LENGTH_LONG).show();
                    UserInfoData userInfoData = response.userData;
                    userLogin(userInfoData);
                }

                base.stopIndicator();
            }

            @Override
            public void onError() {
                Log.e("userRegist onError : ", "");
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
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (facebookCallBackManager != null) {
            if (facebookCallBackManager.onActivityResult(requestCode, resultCode, data)) {
                return;
            }
        }

        if ( requestCode == RC_GOOGLE_SIGN_IN ) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if ( result.isSuccess() ) {
                GoogleSignInAccount account = result.getSignInAccount();

                RequestData req = new RequestData();
                req.authId = account.getId();
                req.joinType = "google";

                Toast.makeText(base, "구글 로그인 성공!!" + account.getId(), Toast.LENGTH_LONG).show();

                Uri profileImg = account.getPhotoUrl();

                UserInfo.getInstance().setParams("authId", req.authId);
                UserInfo.getInstance().setParams("joinType", req.joinType);
                UserInfo.getInstance().setParams("snsEmail", account.getEmail());
                UserInfo.getInstance().setParams("nickname", account.getDisplayName());
                if(profileImg != null)
                    UserInfo.getInstance().setParams("profileImg", account.getPhotoUrl().toString());

                GoogleTokenTask task = new GoogleTokenTask(base, account);
                task.execute("oauth2:https://www.googleapis.com/auth/userinfo.profile");

                userRegist(UserInfo.getInstance().getLoginParams());
            }
            else {

                Toast.makeText(base, "구글 로그인 실패!!" + result.getStatus(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void userLogin(UserInfoData userInfoData)
    {
        base.startIndicator("");

        String joinType = userInfoData.getJoinType();

        UserInfo.getInstance().clearParams();
        if(joinType.equals("email"))
        {
            UserInfo.getInstance().setParams("email", userInfoData.getEmail());
        }
        else
            UserInfo.getInstance().setParams("authId", userInfoData.getAuthId());
        UserInfo.getInstance().setParams("joinType", joinType);
        DataManager.getInstance(base).api.userLogin(base, UserInfo.getInstance().getLoginParams(), new DataInterface.ResponseCallback<UserInfoResultData>() {
            @Override
            public void onSuccess(UserInfoResultData response) {
                Log.e("userLogin onSuccess : ", ""+Utils.getStringByObject(response));
                if(response.getResult().equals(Constants.API_FAIL))
                {
                    Toast.makeText(base, "회원로그인 실패!!" + response.getMessage(), Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(base, "회원로그인 성공!!" + response.getResult(), Toast.LENGTH_LONG).show();
                    UserInfoData userInfoData = response.userData;
                    UserInfo.getInstance().setCurrentUserInfoData(base, userInfoData);
                    BasePreference.getInstance(base).put(BasePreference.ID, userInfoData.getAuthId());
                    BasePreference.getInstance(base).put(BasePreference.JOIN_TYPE, userInfoData.getJoinType());
                    BasePreference.getInstance(base).put(BasePreference.AUTH_ID, userInfoData.getAuthId());
                    BasePreference.getInstance(base).putObject(BasePreference.USERINFO_DATA, userInfoData);
                }

                base.stopIndicator();
            }

            @Override
            public void onError() {
                Log.e("userLogin onError : ", "");
                base.stopIndicator();
            }
        });
    }

    private class GoogleConnectionListener implements GoogleApiClient.OnConnectionFailedListener {

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Logger.log(Logger.LogState.E, "google onConnectionFailed" + connectionResult);
        }
    }
}

package shop.plea.and.gcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import shop.plea.and.common.preference.BasePreference;
import shop.plea.and.common.tool.Logger;

/**
 * Created by kwon on 2017-12-04.
 */

public class MyInstanceIDListenerService extends FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Logger.log(Logger.LogState.E, "Refreshed token: " + refreshedToken);

        BasePreference.getInstance(getApplicationContext()).put(BasePreference.GCM_TOKEN, refreshedToken);
    }
}

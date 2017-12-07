package shop.plea.and.gcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import shop.plea.and.common.tool.Logger;

/**
 * Created by kwon on 2017-12-04.
 */

public class MyFcmListenerService extends FirebaseMessagingService{

    private PushData pushData;

    private class PushData
    {
        String snsid = "";
        String sns = "";
        String typ = "";
        String adroute = "";
        int using;
        int saving;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.log(Logger.LogState.D, "receive gcm message = " + remoteMessage.getData());
    }
}

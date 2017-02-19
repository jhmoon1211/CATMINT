package kr.ac.kpu.catmint_1;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by leesin on 2017-02-19.
 */

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "MyInstance!DService";
    private static final String GCM_NOTICE = "notice";
    private static final String SENDID = "717912239174";

    public RegistrationIntentService(){
        super(TAG);
        Log.d(TAG, "RegistrationIntentService()");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        try{
            //{START register_for_gcm}
            //{START get_token}
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            String token = instanceID.getToken(SENDID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            GcmPubSub pubsub = GcmPubSub.getInstance(this);
            pubsub.subscribe(token, "/topics/" + GCM_NOTICE, null);

        }catch (Exception e){
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }

}
package com.example.pushprimer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.PushPermissionResponseListener;
import com.clevertap.android.sdk.inapp.CTLocalInApp;

import org.json.JSONObject;

public class InAppActivity extends AppCompatActivity implements PushPermissionResponseListener {

    CleverTapAPI cleverTapDefaultInstance;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app);

        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());

    }

    @Override
    public void onResume(){
        super.onResume();

        JSONObject jsonObject = CTLocalInApp.builder()
                .setInAppType(CTLocalInApp.InAppType.ALERT)
                .setTitleText("Get Notified")
                .setMessageText("Enable Notification permission")
                .followDeviceOrientation(true)
                .setPositiveBtnText("Allow")
                .setNegativeBtnText("Cancel")
                .build();

        cleverTapDefaultInstance.promptPushPrimer(jsonObject);
    }

    @Override
    public void onPushPermissionResponse(boolean accepted) {
        Log.i(TAG, "onPushPermissionResponse :  InApp---> response() called accepted=" + accepted);
        if (accepted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                cleverTapDefaultInstance.createNotificationChannel(getApplicationContext(),"manishTest","Default","Default", NotificationManager.IMPORTANCE_MAX,true,"notification_sound.mp3");
            }
        }
    }
}
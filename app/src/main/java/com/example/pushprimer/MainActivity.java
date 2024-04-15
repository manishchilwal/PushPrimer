package com.example.pushprimer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.PushPermissionResponseListener;
import com.clevertap.android.sdk.inapp.CTLocalInApp;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PushPermissionResponseListener {

    CleverTapAPI cleverTapDefaultInstance;

    Button btnNativeDisplay,btnInApp,btnInbox;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());


        //city level permission
        cleverTapDefaultInstance.enableDeviceNetworkInfoReporting(true);

        //latitude and longitude
        Location location = cleverTapDefaultInstance.getLocation();
        cleverTapDefaultInstance.setLocation(location);

        btnNativeDisplay = findViewById(R.id.btnNativeDisplay);
        btnInApp = findViewById(R.id.btnInApp);
        btnInbox = findViewById(R.id.inbox);

        btnNativeDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Native Display Activity
                startActivity(new Intent(MainActivity.this, NativeDisplayActivity.class));
                cleverTapDefaultInstance.pushEvent("Native Display");
            }
        });

        btnInApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to InApp Activity
                startActivity(new Intent(MainActivity.this, InAppActivity.class));
            }
        });


        btnInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InboxActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Call promptPushPrimer from onResume
        JSONObject jsonObject = CTLocalInApp.builder()
                .setInAppType(CTLocalInApp.InAppType.HALF_INTERSTITIAL)
                .setTitleText("Get Notified")
                .setMessageText("Please enable notifications on your device to use Push Notifications.")
                .followDeviceOrientation(true)
                .setPositiveBtnText("Allow")
                .setNegativeBtnText("Cancel")
                .setBackgroundColor("#" + Integer.toHexString(com.example.pushprimer.Constants.WHITE))
                .setBtnBorderColor("#" + Integer.toHexString(com.example.pushprimer.Constants.BLUE))
                .setTitleTextColor("#" + Integer.toHexString(com.example.pushprimer.Constants.BLUE))
                .setMessageTextColor("#" + Integer.toHexString(com.example.pushprimer.Constants.BLACK))
                .setBtnTextColor("#" + Integer.toHexString(com.example.pushprimer.Constants.WHITE))
                .setImageUrl("https://i.ibb.co/BtfMqsm/inbox.png")
                .setBtnBackgroundColor("#" + Integer.toHexString(Constants.BLUE))
                .build();
        // Invoke the Push Primer flow with the created JSON object
        cleverTapDefaultInstance.promptPushPrimer(jsonObject);
        if(cleverTapDefaultInstance.isPushPermissionGranted()){
            onPushPermissionResponse(true);
        }
    }


    // Handle push permission response
    @Override
    public void onPushPermissionResponse(boolean accepted) {
        Log.i(TAG, "onPushPermissionResponse :  InApp---> response() called accepted=" + accepted);
        if (accepted) {
            // Check if the user accepted push notification permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create notification channel if supported
                cleverTapDefaultInstance.createNotificationChannel(getApplicationContext(), "manishTest", "Default", "Default", NotificationManager.IMPORTANCE_MAX, true, "notification_sound.mp3");
            }
        }
    }
}